package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewHavenCountyBParser extends FieldProgramParser {

  public CTNewHavenCountyBParser() {
    this("NORTH BRANFORD", "CT");
  }

  public CTNewHavenCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "ID CODE? CALL ( PLACE/Z ADDR1/Z APT/Z CITY/Z ZIP " +
                        "| ADDR1/Z APT/Z CITY/Z ZIP " +
                        "| PLACE/Z ADDR1/Z APT/Z CITY " +
                        "| ADDR1/Z APT/Z CITY " +
                        "| ADDR2 " +
                        "| ADDR1 DUP? APT? CITY ) " +
          "EMPTY+? ( MAP_X UNIT/Z DATETIME! | UNIT/Z DATETIME! | DATETIME! | MAP_X! UNIT/Z SKIP END ) INFO/N+");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets(
        "FOX GLEN",
        "NEW HARTFORD TOWN LINE",
        "NORTH WINDHAM LINE"
     );
  }

  @Override
  public String getAliasCode() {
    return "CTNewHavenCountyB";
  }

  @Override
  public String getFilter() {
    return "FirePaging@hamdenfirefighters.org,paging@branfordfire.com,paging@easthavenfire.com,paging@easthavenpolice.com,paging@mail.nbpolicect.org,paging@nbpolicect.org,noreply@nexgenpss.com,pdpaging@farmington-ct.org,noreply@whpd.com,page@watertownctpd.com,FirePaging@hamdenfirefighters.org,paging@townofstratford.com,ngpager@rockyhillct.gov,pubsafetypaging@uconn.edu,publicsafety@uchc.edu,nexgen@nbpolicect.org";
  }

  private static final Pattern DELIM1 = Pattern.compile(" *\\| *");
  private static final Pattern MARKER = Pattern.compile("(\\d{10}) +(?:(S\\d{2}) +)?");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +(\\d{6}) (\\d\\d:\\d\\d)(?:[ ,]|$)");
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile(" +\\d{6} [\\d:]+$| +\\d{1,6}$");
  private static final Pattern ADDR_ST_MARKER = Pattern.compile("(.*) (\\d{5} .*)");
  private static final Pattern I_NN_HWY_PTN = Pattern.compile("\\b(I-?\\d+) +HWY\\b");
  private static final Pattern ADDR_END_MARKER = Pattern.compile("Apt ?#:|(?=(?:Prem )?Map -)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("\\S+(?: \\d+)?\\b");
  private static final Pattern MAP_PFX_PTN = Pattern.compile("(?: *(?:Prem )?Map -*)+", Pattern.CASE_INSENSITIVE);
  private static final Pattern MAP_PTN = Pattern.compile("(?:\\d{1,2}(?:[ ,+]+\\d{1,4})?(?:[- ]*\\d?[A-Z]{1,2} *\\d{1,3}(?:-\\d+)?)?(?: ?PP)?|[- ]*[A-Z]{2} *\\d{1,3}|(?:[CMT]-?\\d+(?:-?A)?(?:, [A-Z]\\d[A-Z])?\\b[ &]*)+)\\b *");
  private static final Pattern MAP_EXTRA_PTN = Pattern.compile("\\(Prem Map -*(.*?)\\)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) - (\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)");
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) - (.*)");

  @Override
  public boolean parseMsg(String body, Data data) {

    body = stripFieldStart(body, "no subject / ");

    int pt = body.indexOf("\n\n\nScanned");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace('\n', ' ');

    // See if this is one of the delimited field formats
    String[] flds = DELIM1.split(body);
    if (flds.length > 4) {
      if (!parseFields(flds, data)) return false;
    }

    else {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      setFieldList("ID SRC CODE CALL ADDR APT MAP X CITY UNIT DATE TIME INFO");
      data.strCallId = match.group(1);
      data.strSource = getOptGroup(match.group(2));
      body = body.substring(match.end());

      match =  DATE_TIME_PTN.matcher(body);
      if (match.find()) {
        String date = match.group(1);
        data.strDate = date.substring(2,4) + "/" + date.substring(4,6) + "/" + date.substring(0,2);
        data.strTime = match.group(2);
        data.strSupp = body.substring(match.end()).trim();
        body = body.substring(0,match.start());
      } else {
        match = TRUNC_DATE_TIME_PTN.matcher(body);
        if (match.find()) body = body.substring(0,match.start());
      }

      // Look for an identifiable end of address marker
      //  Either an Apt or map construct
      String field = null;
      String apt = null;
      match = ADDR_END_MARKER.matcher(body);
      if (match.find()) {
        field = body.substring(match.end()).trim();
        body = body.substring(0,match.start()).trim();

        // If this was an app construct, pull out the apartment
        String mark = match.group();
        if (mark.length() > 0) {
          match = ADDR_END_MARKER.matcher(field);
          if (match.find()) {
            apt = field.substring(0,match.start()).trim();
            field = field.substring(match.end()).trim();
          } else {
            match = APT_PTN.matcher(field);
            if (match.lookingAt()) {
              apt = match.group();
              field = field.substring(match.end()).trim();
            }
          }
        }
      }

      body = cleanCity(body, data);

      // Now start working on the address
      StartType st = StartType.START_CALL;
      match = ADDR_ST_MARKER.matcher(body);
      if (match.matches()) {
        st = StartType.START_ADDR;
        data.strCall = match.group(1).trim();
        body = match.group(2);
      }

      // Remove I-nn HWY construct that causes problems
      body = I_NN_HWY_PTN.matcher(body).replaceAll("$1");

      // See what we can do with the address
      int flags = FLAG_NO_IMPLIED_APT;
      if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
      if (field != null) flags |= FLAG_NO_CITY | FLAG_ANCHOR_END;
      else flags |= FLAG_PAD_FIELD;
      parseAddress(st, flags, body, data);
      if (apt != null) data.strApt = append(data.strApt, "-", apt);

      // Several different cases to consider
      // Case 1 - We found an address terminator earlier
      // Everything will have to be parsed from the leftover field, including a
      // possible city name
      boolean noCross = false;
      boolean parseCity = false;
      if (field != null) {
        parseCity = true;
      }

      // Case 2 - we did not find an address terminator
      else {
        field = getLeft();

        // Case 2A - but we found a city
        // In which case we need to parse cross street info from the pad field
        // and the leftover field contains only unit info
        if (data.strCity.length() > 0) {

          String pad = getPadField();

          // If pad starts with a left paren, append parenthesised section to address.
          if (pad.startsWith("(")) {
            pt = pad.lastIndexOf(')');
            if (pt >= 0) {
              data.strAddress = append(data.strAddress, " ", pad.substring(0, pt+1).trim());
              pad = pad.substring(pt+1).trim();
            }
          }

          // What is left is occasionally a city name, but usually a cross street
          if (isCity(pad)) {
            data.strCity = pad;
          } else {
            parseCross(pad, data);
          }
        }

        // Case 2A - no city
        // Everything needs to be parsed from leftover field
        // But we know that it does not contain a city name
        else {
          noCross = isMBlankLeft();
        }
      }

      // Of the three identified cases, option 2A is the only one that has
      // parsed a city name, and is the only one that does not require us to
      // parse information from the leftover field
      if (data.strCity.length() == 0) {

        // Try to parse map information from leftover field
        match = MAP_PFX_PTN.matcher(field);
        if (match.lookingAt()) {
          field = field.substring(match.end());
          noCross = field.startsWith("   ");
          field = field.trim();
          match = MAP_PTN.matcher(field);
          if (match.lookingAt()) {
            data.strMap = stripFieldEnd(match.group().trim(), "&");
            field = field.substring(match.end());
            noCross = field.startsWith("  ");
            field = field.trim();
          }
        }

        // Now we have to split what is left into a cross street and unit
        // If there is a premium map marker between them, things get easy
        field = stripFieldStart(field, "/");
        match = MAP_EXTRA_PTN.matcher(field);
        if (match.find()) {
          parseCross(field.substring(0, match.start()).trim(), data);
          field = field.substring(match.end()).trim();
          if (data.strMap.length() == 0) data.strMap = match.group(1).trim();
        }

        // If not, our best approach is to looking for the first multiple blank delimiter.
        // which is a heck of a lot easier to do now that double blanks are preserved by
        // the getLeft() method.
        else {
          if (!noCross) {
            pt = field.indexOf("  ");
            if (pt >= 0) {
              String cross = field.substring(0,pt);
              if (parseCity) {
                parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, cross, data);
                cross = getStart();
              }
              parseCross(cross, data);
              field = field.substring(pt+2).trim();
            }

            // If we didn't find one, we will have to use the smart address parser to figure out where
            // the cross street information ends
            else {
              flags = FLAG_ONLY_CROSS;
              if (parseCity) flags |= FLAG_ONLY_CITY;
              Result res = parseAddress(StartType.START_ADDR, flags, field);
              if (res.isValid()) {
                res.getData(data);
                field = res.getLeft();
              }
            }
          }
        }

        // If we have not found a city, see if there is one here
        if (parseCity && data.strCity.length() == 0) {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, field, data);
          if (data.strCity.length() > 0) {
            data.strApt = append(data.strApt, "-", getStart());
            field = getLeft();
          }
        }
      }

      // Whatever is left becomes the unit
      field = stripFieldEnd(field, ",");
      data.strUnit = field.replaceAll("  +", " ");

      data.strCity = convertCodes(data.strCity, CITY_CODES);
    }

    // Clean up call code description
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
    } else if ((match = CALL_CODE_PTN.matcher(data.strCall)).matches()) {
      data.strCode = match.group(2);
      data.strCall = match.group(1).trim();
    } else {
      pt = data.strCall.indexOf(" - ");
      if (pt >= 0) {
        String part1 = data.strCall.substring(0, pt).trim();
        String part2 = data.strCall.substring(pt+3).trim();
        if (part1.equals(part2)) data.strCall = part1;
      }
    }

    return true;
  }

  private static final Pattern CROSS_SLASH_PTN = Pattern.compile(" */ *");

  private void parseCross(String cross, Data data) {
    cross = stripFieldStart(cross, "&");
    cross = stripFieldEnd(cross, "&");
    cross = stripFieldStart(cross, "/");
    cross = stripFieldEnd(cross, "/");
    cross = CROSS_SLASH_PTN.matcher(cross).replaceAll(" / ");
    data.strCross = append(data.strCross, " / ", cross);
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  private String cleanCity(String addr, Data data) {
    addr = SR_PTN.matcher(addr).replaceAll("SQ");
    Matcher match = CITY_CODE_PTN.matcher(addr);
    if (match.find()) {
      data.strCity = convertCodes(match.group(1), CITY_CODES);
      addr = match.replaceAll(" ").trim();
    }
    return addr;
  }
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b");
  private static final Pattern CITY_CODE_PTN = Pattern.compile(" *: *(FARM|UNVL)\\b *");

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}");
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("DUP")) return new MyDupField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ZIP")) return new SkipField("\\d{5}");
    if (name.equals("MAP_X")) return new MyMapCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private Pattern LEAD_ZERO_PTN = Pattern.compile("^0+");

  private String cleanAddress(String addr) {
    addr = MBLANK_PTN.matcher(addr).replaceAll(" ");
    addr = LEAD_ZERO_PTN.matcher(addr).replaceFirst("");
    return addr;
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = cleanAddress(field);
      if (field.equals(cleanAddress(getRelativeField(+1)))) return;
      super.parse(field, data);
    }
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = cleanAddress(field);
      super.parse(field, data);;
    }
  }

  private static final Pattern ADDRESS_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = cleanAddress(field);
      String zip = null;
      Matcher match = ADDRESS_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }

      Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field);
      boolean noCity = res.getCity().isEmpty();
      if (zip == null && noCity) return false;
      res.getData(data);;
      if (noCity) data.strCity = zip;

      // If there was no zip code, there probably is a doubled up city
      if (zip == null) {
        field = data.strAddress;
        data.strAddress = "";
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private class MyDupField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-1));
    }
  }

  private static final Pattern APT_PTN2 = Pattern.compile("(?:APT|RM|ROOM|LOT|UNIT)#? *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN2.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyMapCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        field = field.substring(match.end()).trim();
        match = MAP_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strMap = stripFieldEnd(match.group().trim(), "&");
          field = field.substring(match.end()).trim();
        }
      }

      match = MAP_EXTRA_PTN.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start()).trim();
        if (data.strMap.length() == 0) data.strMap = match.group(1).trim();
      }
      data.strCross = stripFieldStart(field, "&");
    }

    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d(?::\\d\\d)?)\\b.*");
  private static final Pattern DIGIT_PTN = Pattern.compile("\\d");
  private static final String TRUNC_DATE_TIME_STR = "NN/NN/NNNN NN:NN";
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace('-', '/');
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        return true;
      }

      // See if this matches a truncated date/time string
      if (field.length() < 3) return false;
      field = DIGIT_PTN.matcher(field).replaceAll("N");
      return TRUNC_DATE_TIME_STR.startsWith(field);
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  @Override
  public String adjustMapAddress(String address) {
    address = GILBERT_EXT.matcher(address).replaceAll("GILBERT RD EXT");
    return super.adjustMapAddress(address);
  }
  private static final Pattern GILBERT_EXT = Pattern.compile("\\bGILBERT EXT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("UCONNHEALTH")) return "FARMINGTON";
    return city;
  }

//  private static CodeTable CALL_CODES = new StandardCodeTable();

  private static final String[] MWORD_STREET_LIST = new String[]{
      "ALEX WARFIELD",
      "ALLINGS CROSSING",
      "APPLE HILL",
      "ASBURY RIDGE",
      "ASHFORD CENTER",
      "ASHLAR VILLAGE",
      "ASPEN GLEN",
      "AUSTIN RYER",
      "AUTUMN RIDGE",
      "AVALON GATE",
      "BABCOCK HILL",
      "BAHRE CORNER",
      "BARNES PARK",
      "BATTERSON PARK",
      "BAY PATH",
      "BEACH HILL",
      "BEACON HILL",
      "BEACON POINT",
      "BEARD SAWMILL",
      "BEAVER HEAD",
      "BEL AIRE",
      "BELLA VISTA",
      "BENHAM HILL",
      "BERRY PATCH",
      "BIDWELL FARM",
      "BIRCH KNOLL",
      "BLACKS HILL",
      "BLISS MEMORIAL",
      "BLODGETT ROY",
      "BLUE HILLS",
      "BLUE RIDGE",
      "BLUE TRAILS",
      "BOOTH HILL",
      "BOSTON POST",
      "BOULDER RIDGE",
      "BOWHAY HILL",
      "BRADFORD WALK",
      "BREEZY HILL",
      "BRIAR HILL",
      "BRITTANY FARMS",
      "BROCKETTS POINT",
      "BRUSHY PLAIN",
      "BUENA VISTA",
      "BUILDING BROOK",
      "BULL HILL",
      "BUNKER HILL",
      "BURNT HILL",
      "BURR HILL",
      "BUSHY HILL",
      "BUSINESS PARK",
      "BUTTON SHOP",
      "CANDLE LITE",
      "CANTON SPRINGS",
      "CAPTAIN THOMAS",
      "CARRIAGE HILL",
      "CEDAR HILL",
      "CEDAR HOLLOW",
      "CEDAR KNOLLS",
      "CEDAR LAKE",
      "CEDAR RIDGE",
      "CEDAR SWAMP",
      "CENTURY HILLS",
      "CHARLTON HILL",
      "CHARTER OAK",
      "CHERRY ANN",
      "CHERRY BROOK",
      "CHERRY HILL",
      "CHIN CLIFT",
      "CIDER MILL",
      "CINNAMON RDG BARNS HILL",
      "CLEAR LAKE MANOR",
      "CLEAR LAKE",
      "CLINT ELDREDGE",
      "CODFISH FALLS",
      "COLD SPRING",
      "COLT HWY",
      "COMMERCE CENTER",
      "COOK HILL",
      "COOPER HILL",
      "COPE FARMS",
      "COPPER BEECH",
      "CORPORATE RIDGE",
      "COSEY BEACH",
      "COUNTRY HILL",
      "COUNTRY SIDE",
      "COUNTY WALK",
      "CPTH CELERON",
      "DANIELS FARM",
      "DAY SPRING",
      "DAYTON HILL",
      "DEER PATH",
      "DEER POND",
      "DEER RUN",
      "DIAMOND GLEN",
      "DORAL FARM",
      "DOWN DRAFT",
      "DUNBAR HILL",
      "DUNNE WOOD",
      "EAST GATE",
      "EAST HILL",
      "EAST MAIN",
      "EAST ROCK PARK",
      "EAST SHORE",
      "EAST WEST SPRING",
      "EASY RUDDER",
      "ELI YALE",
      "ELM COMMONS",
      "FALCON RIDGE",
      "FANS ROCK",
      "FARM GLEN",
      "FARM RIVER",
      "FARM SPRINGS",
      "FARMINGTON CHASE",
      "FAWN HILL",
      "FELLSMERE FARM",
      "FIELD ROCK",
      "FIELD STONE",
      "FIFTY FOOT",
      "FIRST AVE FIRST",
      "FISHER HILL",
      "FLANDERS RIVER",
      "FLAT ROCK",
      "FLAX MILL",
      "FLYING POINT",
      "FOOTE HILL",
      "FOREST HILLS",
      "FOREST PARK",
      "FOREST VIEW",
      "FOX HILL",
      "FOX RIDGE",
      "FOX RUN",
      "FOXON HILL",
      "FRESH MEADOW",
      "GAYLORD FARM",
      "GAYLORD MOUNTAIN",
      "GEO WASHINGTON",
      "GEORGE WASHINGTON",
      "GLEN DEVON",
      "GLEN HAVEN",
      "GLEN RIDGE",
      "GOLDEN HILL",
      "GRAHM MANOR",
      "GRASSY HILL",
      "GRAY FOX",
      "GRAY LEDGE",
      "GREAT CIRCLE",
      "GREAT HILL",
      "GREAT MEADOW",
      "GREAT OAK",
      "GREEN GARDEN",
      "GREEN GLEN",
      "GREEN HILL",
      "GULF BROOK",
      "HALF ACRE",
      "HALF KING",
      "HALF MILE",
      "HALL ACRES",
      "HAMDEN HILLS",
      "HAMDEN PARK",
      "HAMMER MILL",
      "HANES HILL",
      "HANG DOG",
      "HANKS HILL",
      "HARBOR VIEW",
      "HART RIDGE",
      "HAYCOCK POINT",
      "HEMLOCK NOTCH",
      "HERITAGE HILL",
      "HICKORY HILL",
      "HIDDEN BROOK",
      "HIDDEN OAK",
      "HIGH HILL",
      "HIGH MEADOW",
      "HIGH POINT",
      "HIGH RIDGE",
      "HIGH TOP",
      "HIGHWOOD CROSSING",
      "HILLS POINT",
      "HILLSIDE VIEW",
      "HINMAN MEADOW",
      "HOLLY MAR HILL",
      "HONEY POT",
      "HOOP POLE",
      "HOPE HILL",
      "HORSEBARN HILL",
      "HOTCHKISS GROVE",
      "HUCKLEBERRY HILL",
      "HUNTER'S RIDGE",
      "HUNTERS CROSSING BARNES HILL",
      "HUNTING HEIGHTS",
      "HUNTING LODGE",
      "HUNTINGTON OLD GREEN",
      "INDIAN HILL",
      "INDIAN NECK",
      "INDIAN POINT",
      "INDIAN SPRINGS",
      "INDIAN WOODS",
      "ISLAND VIEW",
      "JIM CALHOUN",
      "JOHN STEELE",
      "JOHNNYCAKE MTN",
      "JOHNSONS POINT",
      "JONES HILL",
      "JOYCE II",
      "JUNIPER POINT",
      "KATHERINE GAYLORD",
      "KATIE JOE",
      "KAYE VUE",
      "KIDDS CAVE",
      "KILLAMS POINT",
      "KING HILL",
      "KINGS COLLEGE",
      "KNOB HILL",
      "KROL FARM",
      "LAKE GARDA",
      "LANES POND",
      "LANTERN VIEW",
      "LAURA JANE",
      "LAUREL HILL",
      "LEEDER HILL",
      "LEETES ISLAND",
      "LIME KILN",
      "LINDEN POINT",
      "LINSLEY LAKE",
      "LITTLE BAY",
      "LITTLE OAK",
      "LITTLEBROOK CROSSING",
      "LOCH NESS",
      "LONG HILL CROSS",
      "LONG HILL",
      "MALLARD BROOK",
      "MANSFIELD CITY",
      "MANSFIELD GROVE",
      "MAPLE RIDGE",
      "MARY BELLE",
      "MEADOW WOOD",
      "MEETING HOUSE HILL",
      "MILL PLAIN",
      "MILL POND HEIGHTS",
      "MILL POND",
      "MILL ROCK",
      "MILLER FARMS",
      "MISTY MEADOW",
      "MOOSE MEADOW",
      "MOUNT CARMEL",
      "MOUNT HOPE",
      "MOUNT PLEASANT",
      "MOUNT VERNON",
      "MOUNTAIN SPRING",
      "MOUNTAIN TOP PASS",
      "MOUNTAIN TOP",
      "MOUNTAIN VIEW",
      "MT CARMEL",
      "MTN SPRING",
      "MULBERRY HILL",
      "NORTH ATWATER",
      "NORTH BRANFORD",
      "NORTH EAGLEVILLE",
      "NORTH EAGLVILLE",
      "NORTH EAST",
      "NORTH HIGH",
      "NORTH HILLSIDE",
      "NORTH LAKE",
      "NORTH MAIN",
      "NORTH MOUNTAIB",
      "NORTH MOUNTAIN",
      "NORTH PETERS",
      "NORTH WINDHAM",
      "NOTCH HILL",
      "O'MEARA FARMS",
      "OAK HILL",
      "OAK HOLLOW",
      "OAK RIDGE",
      "OLDE POND",
      "OPENING HILL",
      "ORCHARD HILL",
      "OXEN HILL",
      "PARISH FARM",
      "PARK POND",
      "PARK RIDGE",
      "PARKER FARMS",
      "PARSONAGE HILL",
      "PAUL SPRING",
      "PAWSON LANDING",
      "PAWSON PARKWAY",
      "PAWSON TRAIL",
      "PEARL HARBOR",
      "PEAT MEADOW",
      "PEMBROKE HILL",
      "PERRY MERRILL",
      "PIN OAK",
      "PINE HOLLOW",
      "PINE HURST",
      "PINE ORCHARD",
      "PINE ROCK",
      "PINE TREE SHILLING",
      "PINE TREE",
      "PINE VIEW",
      "PINE WOOD",
      "PLEASANT POINT",
      "POLLY DAN",
      "POMPEY HOLLOW",
      "POND HILL",
      "POND VIEW",
      "POPLAR HILL",
      "PORTAGE CROSSING",
      "POWDER MILL",
      "PRATTLING POND",
      "PROSPECT HILL",
      "PUNCH BROOK",
      "PUTTING GREEN",
      "QUARRY DOCK",
      "QUEEN'S PEAK",
      "RACCIO PARK",
      "RED FOX",
      "RED HILL",
      "RED OAK HILL",
      "RED ROCK",
      "RED STONE",
      "REEDS GAP",
      "RES LEDGEWOOD",
      "ROBERT FROST",
      "ROCK HILL",
      "ROCK HOUSE",
      "ROCK PASTURE",
      "ROCKY LEDGE",
      "ROCKY RIDGE",
      "ROLLING WOOD",
      "ROSE HILL",
      "ROYAL OAK",
      "SAGAMORE COVE",
      "SAINT ANDREWS",
      "SANTA FE",
      "SAW MILL",
      "SCHOOL GROUND",
      "SCHOOL HOUSE",
      "SCOTT SWAMP",
      "SEA HILL",
      "SECRET LAKE",
      "SHINGLE HILL",
      "SHINGLE MILL",
      "SHORT BEACH",
      "SHORT ROCKS",
      "SIDE HILL",
      "SIGNAL HILL",
      "SILAS DEANE",
      "SILVER SANDS",
      "SILVERMINE ACRES",
      "SIR THOMAS",
      "SIX ROD",
      "SKIFF ST EXT",
      "SKY VIEW",
      "SLEEPING GIANT",
      "SLEEPY HOLLOW",
      "SOUND VIEW",
      "SOUNDVIEW VIEW",
      "SOUTH EAGLEVILLE",
      "SOUTH END",
      "SOUTH FOREST",
      "SOUTH MAIN",
      "SOUTH MONTOWESE",
      "SOUTH QUAKER",
      "SOUTH RIDGE",
      "SOUTH SHORE",
      "SOUTH STRONG",
      "SPENO RDG FRANCE",
      "SPICE BUSH",
      "SPORT HILL",
      "SPRING COVE",
      "SPRING GARDEN",
      "SPRING ROCK",
      "SQUAW BROOK",
      "ST ANDREW",
      "ST ANDREWS",
      "ST JAMES",
      "ST MONICA",
      "ST SYLVAN",
      "STILL HILL",
      "STONE HILL",
      "STONE RIDGE",
      "STONY CREEK",
      "STONY HILL",
      "SUGAR CAMP",
      "SUMMER ISLAND",
      "SUNNY MEADOW",
      "SUNNY SIDE",
      "SUNSET BEACH",
      "SUNSET HILL",
      "SUNSET MANOR",
      "SYBIL CREEK",
      "TALCOTT FOREST",
      "TALCOTT GLEN",
      "TALCOTT NOTCH",
      "TALL PINES",
      "TEDWIN FARMS",
      "TEN ROD",
      "THE MEWS CENTURY HILLS",
      "THIMBLE FARMS",
      "THIMBLE ISLAND",
      "THISTLE MEADOW",
      "THOMPSON HILL",
      "TIMBER BROOK",
      "TOWERS LOOP",
      "TOWN FARM",
      "TOWN LINE",
      "TOWNE HOUSE",
      "TOWNER SWAMP",
      "TRAP FALLS",
      "TROLLEY BRIDGE",
      "TROTTERS GLEN",
      "TROUT BROOK",
      "TUNXIS MEAD",
      "TUNXIS VILLAGE",
      "TURTLE BAY",
      "TWIN LAKE",
      "TWIN LAKES",
      "TWO MILE",
      "TYLER MILL",
      "UCONN HEALTH",
      "VALLE VIEW",
      "VALLEY BROOK",
      "VALLEY VIEW",
      "VAN HORN",
      "VICTOR HILL",
      "VILLAGE SQUARE",
      "WALDEN GREEN",
      "WARNER HILL",
      "WASHINGTON MANOR",
      "WATCH HILL",
      "WATERING POND",
      "WAVERLY PARK",
      "WESLEY HEIGHTS",
      "WEST AVON",
      "WEST CAMPUS",
      "WEST CHIPPEN HILL",
      "WEST CLARK",
      "WEST DISTRICT",
      "WEST FARMS",
      "WEST GATE",
      "WEST MAIN",
      "WEST MEATH",
      "WEST MELOY",
      "WEST MOUNTAIN",
      "WEST POINT",
      "WEST SIDE",
      "WEST SLOPE",
      "WEST SPRING",
      "WEST TODD",
      "WEST WOODS",
      "WHARTON BROOK",
      "WHISPERING HILLS",
      "WHITE HOLLOW",
      "WHITE PLAINS",
      "WHITING FARM",
      "WILD FLOWER",
      "WILLOW BROOK",
      "WIND SOCK",
      "WINDING BRIDGE",
      "WINDMILL HILL",
      "WINDY HILL",
      "WISK KEY WIND",
      "WOLCOTT WOODS",
      "WOLF PIT",
      "WOOD ACRES",
      "WOODCHUCK HILL",
      "WOODS EDGE",
      "WOODS HILL",
      "YOUNG'S APPLE ORCHARD",
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "2CO ALARM SOUNDING - NO SYMPTOMS",
      "2FIRE - MISCELLANEOUS",
      "2MEDICAL - MISCELLANEOUS",
      "2MEDICAL - UNRESPONSIVE",
      "ABD PAIN/PROB- P-1 NOT ALERT",
      "ABD PAIN/PROB- P-1 PAIN ABOVE NAVEL = 45",
      "ABD PAIN/PROB P-2",
      "ABDOMINAL PAIN - PRI 1 -",
      "ABDOMINAL PAIN - PRI 2 -",
      "ABDOMINAL PAIN--FEMALE W/PAIN ABOVE NAVEL >45-PRI 1",
      "ABDOMINAL PAIN-FEMALE FAINTING/NEAR FAINT 12-50-PRI 1 -",
      "ABNORMAL BREATHING DIFFICULTY SPEAKING - ASTHMA",
      "ACCIDENT MV",
      "ACCIDENT - MV INJ ALPHA",
      "ACCIDENT - MV INJ BRAVO",
      "ACCIDENT - MV INJ BRAVO RED",
      "ACCIDENT- MV INJ DELTA",
      "ACCIDENT MV W/INJURIES",
      "ACTIVATED FIRE ALARM",
      "AFA",
      "AFA - CHECK ALARM",
      "AFA - COMMERCIAL/INDUSTRIAL",
      "AFA - HIGH LIFE HAZARD",
      "AFA -  MULTI-FAMILY RESIDENTIAL",
      "AFA - MULTI-FAMILY RESIDENTIAL",
      "AFA - RESIDENTIAL",
      "AFA DAY RESPONSE",
      "AFA NIGHT RESPONSE",
      "AFA RESIDENTIAL",
      "ALARM-FIRE",
      "ALARM - FIRE",
      "ALARM - FIRE WATERFLOW",
      "ALARM - MEDICAL",
      "ALARMS-HOLDUP/PANIC/DURESS",
      "ALLERG/STING",
      "ALLERG/STING P-1",
      "ALPHA MEDICAL",
      "ALS EMS RESPONSE",
      "ANIMAL BITE- P-1 CHEST/NECK INJURY W/ SOB",
      "ANML BITE - SUPRFICIAL BITES - PRI 2 -",
      "ASSAULT - NOT DNGRS",
      "ASSAULT/SEX ASSAULT POSS DANG. BODY AREA",
      "ASSIST INVALID",
      "ASSIST POLICE",
      "AUTO ACC RESPONSE",
      "AUTOMATIC FIRE ALARM",
      "AUTOMATIC FIRE ALARM - COMMERCIAL",
      "BACK PAIN - PRI 2",
      "BACK PAIN P-1",
      "BACK PAIN SUSP ANEURYSM- PRI 1",
      "BEHAVORIAL",
      "BEHAVORIAL (UNKNOWN) - PRI 1 -",
      "BEHAVORIAL - THREATENING SUICIDE - PRI 1",
      "BEHAVORIAL - UNKNOWN - PRI 1",
      "BLS EMS RESPONSE",
      "BREACH OF PEACE",
      "BREATH PROB P-1",
      "BREATH PROB- P-1 ABNORMAL",
      "BREATH PROB- P-1 NOT ALERT",
      "BRUSH FIRE",
      "BRUSH FIRE - UNKNOWN",
      "BRUSH FIRE/CAMP FIRE",
      "BRUSH FIRE TWIN",
      "BUILDING DAMAGE",
      "BUILDING LOCKOUT",
      "C.O. ALARM",
      "CAR FIRE",
      "CAR LOCKOUT",
      "CARBON MONOXIDE",
      "CARDIAC / RESP. ARREST",
      "CARDIAC/RESP. ARREST-OBVIOUS/EXPECTED DEATH - PRI 1 -",
      "CELLAR PUMP/WATER EMERGENCY",
      "CHARLIE MEDICAL",
      "CHARLIE MEDICAL TF1",
      "CHARLIE MEDICAL TF3",
      "CHARLIE MEDICAL TF4A",
      "CHECK APPLIANCE",
      "CHECK ELECTRICAL HAZARD",
      "CHECK ELECTRICAL ODOR",
      "CHECK AN ODOR - FUEL (INSIDE)",
      "CHECK ODOR - INSIDE",
      "CHEST PAIN P-1",
      "CHEST PAIN- P-1 BREATH NORMALLY = 35",
      "CHEST PAIN - PRI 1 - BREATH NORMAL >35",
      "CHEST PAIN- P-1 CLAMMY OR COLD SWEATS",
      "CHEST PAIN  - PRIORITY 1 -",
      "CHEST PAIN CLAMMY - PRI. 1 -",
      "CHEST PAIN-CLAMMY/COLD SWEATS - PRI. 1",
      "CHEST PAIN DIFFICULTY SPEAKING",
      "CHEST PAIN, NOT ALERT - PRI. 1 -",
      "CHEST PAIN, SOB - PRI. 1 -",
      "CHOKING, PARTIAL OBSTR. - PRI. 1 -",
      "CITIZEN ASSIST",
      "CO ALARM (NO SYMPTOMS)",
      "CO ALARM NO/UNK MEDICAL SX",
      "CO ALARM NO MEDICAL SX - HIGH RISE",
      "CO ALARM NO MEDICAL SX -  MULTI-FAMILY RESIDENTIAL",
      "CO ALARM NO MEDICAL SX - MULTI-FAMILY RESIDENTIAL",
      "CO ALARM W/ MEDICAL SX",
      "CO DETECTOR/NO MED SYMPTOMS",
      "CO W/O SYMPTOMS",
      "CODE P-1",
      "CODE- P-1 NOT BREATHING AT ALL",
      "CODE- P-1 OBVIOUS DEATH  NOT RECENT",
      "COVER ASSIGNMENT (IN CITY)",
      "COVER/RELOCATE TO FIRE HQ",
      "DANGEROUS CONDITION",
      "DELTA MEDICAL TF1",
      "DIABETIC",
      "DIABETIC P-1",
      "DIABETIC- P-1 NOT ALERT",
      "DIABETIC- P-2 ALERT AND NORMAL",
      "DIABETIC (ALERT)  - PRI 2 -",
      "DIABETIC, AMS - PRI 1 -",
      "DIABETIC, SOB - PRI 1 -",
      "DIFF. BREATHING - PRI 1 -",
      "DIFF. BREATHING, CLAMMY - PRI 1 -",
      "DIFF. BREATHING, NOT ALERT - PRI 1 -",
      "DIFF. BREATHING-DIFF SPEAKING - PRI 1 -",
      "DUMPSTER FIRE",
      "E-MUTUAL AID AMBULANCE REQUEST",
      "E-MUTUAL AID MEDIC INTERCEPT REQUEST",
      "EDP",
      "EFD IN PROGRESS",
      "ELECTRICAL ISSUE - INVEST",
      "ELEVATOR PROBLEM - W/ OCCUPANTS",
      "ELEVATOR RESCUE",
      "EMD IN PROGRESS",
      "EMERGENCY",
      "EMS",
      "EMS ASSIST",
      "EMS INCIDENT",
      "ENTRAPMENT (NO MEDICAL OR HAZARD)",
      "EPD IN PROGRESS",
      "EYE INJURY (MEDICAL) - PRI 2 -",
      "EYE PROB/INJ- P-2 MEDICAL EYE PROBLEMS",
      "EYE PROB/INJ- P-2 MOD EYE INJURIES",
      "F-BOAT COLLISION PEOPLE IN WATER - COASTAL",
      "F-STRUCTURE FIRE BUILDING/STRUCTURE OVER WATER",
      "F-STRUCTURE FIRE BUILDING/STRUCTURE OVER WATER TRAPPED",
      "F-STRUCTURE FIRE RESIDENTIAL (MULTI.)",
      "F-STRUCTURE FIRE RESIDENTIAL (SING.)",
      "F-STRUCTURE FIRE RESIDENTIAL (SING.) ODOR ONLY",
      "F-STRUCTURE FIRE RESIDENTIAL (SING.) TRAPPED",
      "FAINTING >35 W/CARDIAC HX",
      "FAINTING,  ALERT",
      "FAINTING,  ALERT - PRI 2 -",
      "FAINTING -  ALERT >35 - PRI 2",
      "FAINTING >35 W/CARDIAC HX - PRI 1",
      "FALL - FALL DOWN STAIRS - PRI 1",
      "FALL P-1",
      "FALL- P-1 NOT ALERT  STILL DOWN",
      "FALL- P-1 POSS DANG BODY AREA",
      "FALL- P-1  UNKNOWN STILL DOWN",
      "FALL P-2",
      "FALL- P-2 LIFT ASSIST  STILL DOWN",
      "FALL- P-2 NO DANG BODY AREA W/ DEFORM",
      "FALL- P-2 NOT DANG BODY AREA",
      "FALL- P-2 NOT DANG BODY AREA STILL DOWN",
      "FALL- P-2 POSS DANG BODY AREA  STILL DOWN",
      "FALL - PRI 2 -",
      "FALL (NOT ALERT)",
      "FALL - NON DANGEROUS PRI 2",
      "FALL - NON RECENT PRI 2",
      "FALL - POSS. DANG. AREA- PRI 1",
      "FALL, POSS. DANG. AREA- PRI 1 -",
      "FALL - PUB ASST - PRI 2",
      "FALL, PUB ASST - PRI 2 -",
      "FALL PUBLIC ASSIST(NO INJURY)",
      "FALL, UNKNOWN",
      "FALL -  UNKNOWN - PRI 1",
      "FALL, UNKNOWN - PRI 1 -",
      "FILL BOX ALARM",
      "FIRE - BRUSH FIRE",
      "FD ALPHA",
      "FD ASSIST/SERVICE CALL",
      "FD BRAVO",
      "FD CHARLIE",
      "FD DELTA",
      "FD ECHO",
      "FD FIRE ALARM",
      "FD GAS LEAKS/GAS EMERGENCIES",
      "FD STRUCTURE FIRE",
      "FD VEHICLE FIRE (SPECIFY TYPE)",
      "FIRE - BRUSH / OUTSIDE",
      "FIRE - CO ALARM",
      "FIRE - EMS MUTUAL AID",
      "FIRE - FLUID SPILL",
      "FIRE - MUTUAL AID",
      "FIRE MUTUAL AID UCH",
      "FIRE - MV",
      "FIRE - OTHER",
      "FALL - PRI 2 -",
      "FIRE - PUBLIC ASSIST",
      "FALL - UNKNOWN - PRI 1",
      "FIRE - SMOKE/GAS INVEST INSIDE",
      "FIRE - SMOKE/GAS INVEST OUTSIDE",
      "FIRE - STRUCTURE FIRE",
      "FIRE - VEHICLE",
      "FIRE ALARM",
      "FIRE ALARM  00B12",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM COMMERCIAL/WATERFLOW/APARTMENTS",
      "FIRE ALARM-HIGH RISE/TARGET HAZARD",
      "FIRE ALARM-MULTIPLE DEVICES",
      "FIRE ALARM RESIDENTIAL (ONE & TWO FAMILY HOMES)",
      "FIRE ALARM UCH",
      "FIRE APPLIANCE",
      "FIRE CAPTAIN NOTIFICATION - NO NFIRS",
      "FIRE DEPARTMENT UNLOCK",
      "FIRE CALL",
      "FIRE CALL UCH",
      "FIRE OUT REPORT W/ ODOR OF SMOKE",
      "FIRE RESPONSE - ODOR OF SMOKE",
      "FIRE RESPONSE MUTUAL AID",
      "FIRE STRUCTURE",
      "FIREWORKS",
      "FUEL SPILL - COASTAL WATER - OUTSIDE",
      "F-STRUCTURE FIRE HIGH LIFE HAZ",
      "F-STRUCTURE FIRE - RESIDENTIAL",
      "F-STRUCTURE FIRE - RESIDENTIAL (SINGLE)",
      "F-STRUCTURE FIRE (FIRE OUT)",
      "F-STRUCTURE FIRE APPLIANCE (CONTAINED)",
      "F-STRUCTURE FIRE RESIDENTIAL (SINGLE)",
      "F-STRUCTURE FIRE RESIDENTIAL (MULTI)",
      "F-STRUCTURE FIRE RESIDENTIAL (MULTI)    - ODOR OF SMOKE",
      "F-STRUCTURE FIRE UNKNOWN SITUATION (INVESTIGATION)",
      "FLUID SPILL",
      "FUEL SPILL",
      "FUEL SPILL - INLAND WATER - OUTSIDE",
      "FUEL SPILL - MINOR - OUTSIDE",
      "FUEL SPILL (SMALL LESS THAN 50G)",
      "GAS ODOR (INSIDE)",
      "GAS ODOR (OUTSIDE)",
      "HAZ-MAT I",
      "HAZMAT - ACTIVE CHEMICAL LEAK / SPILL",
      "HAZMAT - ACTIVE GAS LEAK",
      "HAZMAT-CONTAINED- CHEMICAL",
      "HAZMAT - INVESTIGATION",
      "HAZMAT - UNCONTAINED",
      "HEAD ACHE P-1",
      "HEAD ACHE- P-2 BREATHING NORMALLY",
      "HEMORR/LAC- P-1 POSSIBLY DANGEROUS  MEDICAL",
      "HEART PROB",
      "HEART PROB P-1",
      "HEART PROB- P-1 CLAMMY OR COLD SWEATS",
      "HEART PROBLEMS - CARDIAC HX - PRI 1",
      "HEART PROBLEMS - SOB- PRI 1",
      "HEAVY SMOKE INVESTIGATION OUTSIDE",
      "HEM. / LAC. DANGEROUS BLEED - PRI 1",
      "HEM. / LAC. DIFF. BREATHING - PRI 1 -",
      "HEM. / LAC. SERIOUS BLEED - PRI 1",
      "HEM. / LAC. -  NON DANGEROUS  PRI 2",
      "HEM. / LAC. - NON DANGEROUS PRI 2",
      "HEMORRHAGE THROUGH TUBES",
      "HEMORR/LAC P-1",
      "HEMORR/LAC- P-1 NOT ALERT  MEDICAL",
      "ILLEGAL BURNING",
      "INEFFECTIVE BREATHING - PRI 1 -",
      "INJURY P-1",
      "INJURY- P-2 NOT DANGEROUS BODY AREA WITH DEFORMITY",
      "INLAND WATER RESCUE",
      "INTERFACILITY",
      "INTERFACILITY P1",
      "INTOXICATED PERSON",
      "INVESTIGATE(OTHER NON-EMERGENT)",
      "INVESTIGATION",
      "LIFT ASSIST",
      "LIFT ASSIST (NO FALL)",
      "LIGHT SMOKE INVESTIGATION OUTSIDE",
      "LOCK IN/OUT - BUILDING",
      "LOCK IN - VEHICLE",
      "LOCKOUT/LOCKIN EMERGENCY",
      "LOCKOUT - VEHICLE",
      "LONG FALL",
      "MARINE RESCUE",
      "MEDICAL ALPHA",
      "MEDICAL BRAVO",
      "MEDICAL CHARLIE",
      "MEDICAL DELTA",
      "MEDICAL ALARM ACTIVATION - PRI 1",
      "MEDICAL ALARM ACTIVATION - PRI 1 -",
      "MEDICAL ALERT ALARM (NO VOICE)",
      "MEDICAL - ALS CALL",
      "MEDICAL-A RESPONSE",
      "MEDICAL-A RESPONSE N I-95  RAMP 43 NORTH/FIRST AVE",
      "MEDICAL - BLS CALL",
      "MEDICAL - BLS CALL 0LLOT",
      "MEDICAL - BLS CALL NORTH",
      "MEDICAL-B RESPONSE",
      "MEDICAL CALL",
      "MEDICAL CALL ALPHA RESPONSE",
      "MEDICAL CALL BRAVO RESPONSE",
      "MEDICAL CALL CHARLIE RESPONSE",
      "MEDICAL CALL DELTA RESPONSE",
      "MEDICAL CALL ECHO RESPONSE",
      "MEDICAL CALL OMEGA RESPONSE",
      "MEDICAL  CANTON",
      "MEDICAL-C RESPONSE",
      "MEDICAL-D RESPONSE",
      "MEDICAL EMERGENCY",
      "MEDICAL MEDA",
      "MEDICAL MEDB",
      "MEDICAL MEDC",
      "MEDICAL MEDD",
      "MEDICAL MEDE",
      "MEDICAL -MUTUAL AID UCH",
      "MEDICAL ON UCH PROPERTIES",
      "MEDICAL OTHER LOCATIONS NOT LISTED",
      "MEDICAL SIMSBURY",
      "MEDICAL TRANSFER UCH",
      "MEDICAL TRANSPORT UCH",
      "MEDICAL UCH",
      "MOTOR VEHICLE ACCIDENT",
      "MOTOR VEHICLE LOCK OUT",
      "MOUNTAIN / TECHNICAL RESCUE",
      "MUTA - E-MUTUAL AID AMBULANCE REQUEST",
      "MUTP - E-MUTUAL AID MEDIC INTERCEPT REQUEST",
      "MUTUAL AID",
      "MUTUAL AID - FIRE",
      "MUTUAL AID INCIDENT MEDICAL",
      "MUTUAL AID - MEDICAL",
      "MUTUAL AID PARAMEDIC",
      "MUTUAL AID SOUTH",
      "MUTUAL AID STANDBY",
      "MV ACCIDENT",
      "MVA",
      "MVA/ INJURIES REPORTED - RADIO",
      "MVA-ENTRAPMENT-RADIO",
      "MVA (EXTRICATION OR ROLLOVER)",
      "MVA - INJURY",
      "MVA - MINOR INJURY",
      "MVA - NO INJURY",
      "MVA-ROLLOVER - RADIO",
      "MVA W/ ENTRAPMENT",
      "MVA W/ INJURIES",
      "MVA W/INJURY",
      "MVA W/INJURIES",
      "MVA WITH INJURIES",
      "MVA WITHOUT INJURIES",
      "MV CRASH-TRAFFIC CRASH (NO INJURY)",
      "MV CRASH-TRAFFIC CRASH (NO INJURY)-B",
      "MV CRASH-TRAFFIC CRASH(UNKNOWN INJURY)",
      "MV CRASH-TRAFFIC CRASH (WITH INJURY)",
      "MV CRASH-TRAFFIC CRASH (WITH INJURY)-B",
      "MV CRASH-TRAFFIC CRASH (WITH INJURY)-R",
      "NATURAL GAS LEAK",
      "NATURAL / LP GAS - ODOR - COMMERCIAL/INDUSTRIAL BUILDING -",
      "NATURAL / LP GAS -  ODOR - MULTI-FAMILY RESIDENTIAL",
      "NATURAL / LP GAS - ODOR OUTSIDE",
      "NATURAL / LP GAS - ODOR - RESIDENTIAL",
      "NATURAL / LP GAS -  ODOR - RESIDENTIAL",
      "NATURAL/LP GAS - LEAK/ODOR - COMMERCIAL/INDUST BLDG",
      "NATURAL/LP GAS TANK - OUTSIDE - < 5 GALS - ODOR ONLY",
      "NATURAL/LP GAS TANK - OUTSIDE - > 5 GALS - ODOR ONLY",
      "OD/INGEST P-1",
      "OD/INGEST- P-1 ABN BREATH  INTENT",
      "OD/INGEST- P-1 UNCONSCIOUS  ACCIDENT.",
      "ODOR OF BURNING",
      "ODOR OF NATURAL GAS",
      "ODOR OF SMOKE INDOORS",
      "ODOR OF SMOKE OUTDOORS",
      "OMEGA MEDICAL",
      "OUTSIDE FIRE",
      "OUTSIDE FIRE - EXTINGUISHED",
      "OUTSIDE FIRE - INVEST",
      "OUTSIDE FIRE - INVEST -UNKNOWN",
      "OUTSIDE FIRE - OTHER",
      "OUTSIDE FIRE - UNKNOWN",
      "OVERDOSE - DIFF. BREATHING - PRI 1",
      "OVERDOSE - NOT ALERT - PRI 1",
      "OVERDOSE UNCON.",
      "PANDEMIC - DIFF SPEAK BETWEEN BREATH - PRI 1",
      "PANDEMIC - FLU SYMPTOMS ONLY - PRI 2",
      "PANDEMIC - HIGH RISK - PRI 1",
      "PANDEMIC - SOB W/FLU SYMPTOMS - PRI 1",
      "PANDEMIC - SOB W/MULTI FLU - PRI 1",
      "PANDEMIC - SOB W/SINGLE FLU - PRI 1",
      "PERSON DOWN- P-1 MEDICAL ALARMNOTIFICATIONS NO INFO",
      "PERSON DOWN P-2",
      "PERSON STUCK IN ELEVATOR",
      "POSS HEART - PRI 1 -",
      "POSS HEART, SOB",
      "POST CHOKING",
      "POST CHOKING - PRIORITY 2 -",
      "POST SEIZURE",
      "PREG./CHILDBIRTH - ABD PAIN <6 MOS - PRI 1",
      "PROPANE LEAK",
      "PSYCHOLOGICAL STATUS",
      "PUBLIC ASSIST",
      "PUBLIC ASSIST - ENGINE",
      "PUBLIC ASSIST -  RESCUE",
      "PUBLIC ASSISTANCE FD",
      "PUBLIC SERVICE",
      "PUBLIC SERVICE (FIRE)",
      "RAPID INTERVENTION TEAM",
      "REPORT OF SMOKE IN AREA",
      "RESCUE - ELEVATOR ENTRAPMENT",
      "RESIDENTIAL LOCKOUT",
      "RESET FIRE ALARM",
      "SEIZURE-CONTINUOUS/MULTIPLE- PRI. 1",
      "SEIZURE-EFFECT BREATH NOT VERIFIED >35- PRI. 1",
      "SEIZURE - NOT ALERT - PRI 1",
      "SEIZURE - NOT SIEZING/EFFECT BREATH PRI. 1",
      "SEIZURE P-1",
      "SEIZURE P-2",
      "SEIZURE(S)",
      "SERVICE CALL - NON-EMERGENCY",
      "SICK CALL - AMS - PRI 1",
      "SICK CALL P-1",
      "SICK CALL- P-1 ABNORMAL BREATHING",
      "SICK CALL- P-1 ALTERED LEVEL OF CONSCIOUSNESS",
      "SICK CALL- P-1 NOT ALERT",
      "SICK CALL P-2",
      "SICK CALL- P-2 BLOOD PRESSURE ABNORMALITY NO SX",
      "SICK CALL- P-2 FEVER/CHILLS",
      "SICK CALL- P-2 GENERAL WEAKNESS",
      "SICK CALL- P-2 NO PRIORITY SYMPTOMS",
      "SICK CALL- P-2 UNKNOWN",
      "SICK CALL - ABNORMAL B/P - PRI 2",
      "SICK CALL - ABNORMAL B/P - PRI 2 -",
      "SICK CALL, AMS - PRI 1 -",
      "SICK CALL,  COND 2-11 NOT IDENTIFIED",
      "SICK CALL, COND 2-11 NOT IDENTIFIED",
      "SICK CALL DIFF BREATHING  - PRI 1 -",
      "SICK CALL - DIZZINESS - PRI 2 -",
      "SICK CALL - DIZZINESS/VERITIGO - PRI 2",
      "SICK CALL - FEVER/CHILLS - PRI 2",
      "SICK CALL, GEN. WEAKNESS",
      "SICK CALL - GEN. WEAKNESS - PRI 2",
      "SICK CALL IMMOBILITY",
      "SICK CALL - IMMOBILITY - PRI 2",
      "SICK CALL - NOT ALERT  - PRI 1",
      "SICK CALL - NOT ALERT - PRI 1",
      "SICK CALL, NOT ALERT",
      "SICK CALL NOT WELL / ILL",
      "SICK CALL - NOT WELL / ILL - PRI 2",
      "SICK CALL OTHER PAIN",
      "SICK CALL - OTHER PAIN - PRI 2",
      "SICK CALL - UNKNOWN - PRI 1",
      "SICK CALL VOMITING - PRI 2 -",
      "SICK PERSON NAUSEA",
      "SICK PERSON NO PRIORITY SYMPTOMS",
      "SMOKE IN A BUILDING",
      "SMOKE INVESTIGATION INSIDE",
      "SMOKE ODOR INVESTIGATION",
      "SPORTS EVENT DETAIL",
      "STATE TASK FORCE 51 WEST",
      "STILL",
      "STILL ASSIGNMENT",
      "STILL - ONE ENGINE",
      "STROKE",
      "STROKE - NOT ALERT",
      "STROKE - SPEECH PROBLEM - PRI 1",
      "STROKE - SPEECH PRBLM - PRI 1 -",
      "STROKE/TIA P-1",
      "STROKE/TIA- P-1 SPEECH PROBLEMS  <12 HOURS SINCE SX",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE COMMERCIAL/  HAZMAT",
      "STRUCTURE FIRE COMMERCIAL/INDUSTRIAL",
      "STRUCTURE FIRE RESIDENTIAL (MULTI)",
      "STRUCTURE FIRE RESIDENTIAL (MULTI)   -TRAPPED (S)",
      "STRUCTURE FIRE RESIDENTIAL (SINGLE)",
      "STRUCTURE FIRE RESIDENTIAL (SINGLE)",
      "SUICIDE / ATTEMPT",
      "TASK FORCE 1 TO BERLIN/NEWINGTON",
      "TEST - TEST1234567890",
      "TRAFFIC/TRANS ACCID/ INJURIES",
      "TRAFFIC STOP",
      "TRANSFORM FIRE",
      "TRAUMATIC INJ. -NON RECENT - PRI 2",
      "TRAUMATIC INJ., NOT DANG.",
      "UCH FD-BEHAVIORAL INTERVENTION",
      "UNCON. - AGONAL/INEFFECT BREATHING - PRI 1",
      "UNCON. - SOB - PRI 1",
      "UNCON. EFF. BREATHING",
      "UNCON/FAINT P-1",
      "UNCON/FAINT- P-1 NOT ALERT",
      "UNCON/FAINT- P-1 FAINTING EPISODE/ALERT = 35 W/ CARDIAC HX",
      "UNCON/FAINT- P-1 UNCON -- ABNORMAL BREATHING",
      "UNCON/FAINTING - NOT ALERT - PRI 1",
      "UNCON/FAINTING NOT ALERT - PRI 1 -",
      "UNCON/FAINTING, SOB - PRI 1 -",
      "UNKNOWN",
      "UNKNOWN - PT MOVING/TALKING - PRI 2",
      "UNKNOWN - PT MOVING/TALKING - PRI 2 -",
      "UNKNOWN MEDICAL - PRI 1",
      "UTILITY FIRE (NO EXPOSURE)",
      "VEHICLE FIRE",
      "VEHICLE FIRE (NO EXPOSURE)",
      "WATER EMERGENCY",
      "WATER CONDITION",
      "WATER PROBLEM",
      "WATER PROBLEM RESIDENTIAL",
      "WATER RESCUE",
      "WATERCRAFT OR BOATER IN DISTRESS",
      "WELFARE CHECK",
      "WELFARE CHECK - FD",
      "WIRE DOWN",
      "WIRES DOWN",
      "WIRES DOWN/BURNING",
      "WIRES DOWN - CHECK FOR HAZARDS",
      "WORKING FIRE/RIT"
  );

  private static final String[] CITY_LIST = new String[]{

      // New Haven County
      "BURLINGTON",
      "BRANFORD",
      "BRISTOL",
      "CANTON",
      "EAST HAVEN",
      "FARMINGTON",
      "GUILFORD",
      "HAMDEN",
      "MILFORD",
      "NEW HAVEN",
      "NORTH BRANFORD",
      "NORTH HAVEN",
      "NORTHFORD",
      "UNIONVILLE",
      "WALLINGFORD",
      "WEST HARTFORD",
      "WEST HAVEN",
      "WLFD",
      "YALESVILLE",

      // Fairfield County
      "FAIRFIELD",
      "BRIDGEPORT",
      "EASTON",
      "MONROE",
      "SHELTON",
      "STRATFORD",
      "TRUMBULL",
      "WESTON",

      // Hartford County
      "AVON",
      "BERLIN",
      "BLOOMFIELD",
      "BRISTOL",
      "BROAD BROOK",
      "BURLINGTON",
      "CANTON",
      "EAST GRANBY",
      "EAST HARTFORD",
      "EAST WINDSOR",
      "ENFIELD",
      "FARMINGTON",
      "GLASTONBURY",
      "GRANBY",
      "HARTFORD",
      "HARTLAND",
      "MANCHESTER",
      "MARLBOROUGH",
      "NEW BRITAIN",
      "NEWINGTON",
      "PLAINVILLE",
      "ROCKY HILL",
      "SIMSBURY",
      "SOUTH WINDSOR",
      "SOUTHINGTON",
      "SUFFIELD",
      "UCONNHEALTH",
      "UNIONVILLE",
      "WEST HARTFORD",
      "WETHERSFIELD",
      "WINDSOR",
      "WINDSOR LOCKS",
      "WINDSOR LOCKS EAST",

      // Middlesex County
      "CROMWELL",
      "DURHAM",

      // Litchfield County
      "TORRINGTON",

      "BARKHAMSTED",
      "BETHLEHEM",
      "BRIDGEWATER",
      "CANAAN",
      "COLEBROOK",
      "CORNWALL",
      "GOSHEN",
      "HARWINTON",
          "NORTHWEST HARWINTON",
      "KENT",
          "SOUTH KENT",
      "LITCHFIELD",
          "BANTAM",
      "MORRIS",
      "NEW HARTFORD",
      "NEW MILFORD",
          "GAYLORDSVILLE",
      "NORFOLK",
      "NORTH CANAAN",
      "PLYMOUTH",
          "TERRYVILLE",
      "ROXBURY",
      "SALISBURY",
          "LAKEVILLE",
          "LIME ROCK",
      "SHARON",
      "THOMASTON",
      "WARREN",
      "WASHINGTON",
          "NEW PRESTON",
      "WATERTOWN",
          "OAKVILLE",
      "WINCHESTER",
          "WINSTED",
      "WOODBURY",
          "HOTCHKISSVILLE",

      // New London County
      "FRANKLIN",
      "LEBANON",

      // Tolland County
      "ANDOVER",
      "BOLTON",
      "COLUMBIA",
      "COVENTRY",
      "ELLINGTON",
      "HEBRON",
      "MANSFIELD",
      "SOMERS",
      "STAFFORD",
      "TOLLAND",
      "UNION",
      "VERNON",
      "WILLINGTON",

      "COVENTRY LAKE",
      "SOUTH COVENTRY",
      "CRYSTAL LAKE",
      "STAFFORD SPRINGS",
      "STORRS",
      "CENTRAL SOMERS",
      "ROCKVILLE",
      "MASHAPAUG",

      "WAREHOUSE POINT",

      "UCONN",

      // Windham county
      "ASHFORD",
      "BROOKLYN",
          "EAST BROOKLYN",
      "CANTERBURY",
      "CHAPLIN",
      "EASTFORD",
      "HAMPTON",
      "KILLINGLY",
          "DANIELSON",
      "PLAINFIELD",
          "CENTRAL VILLAGE",
          "MOOSUP",
          "PLAINFIELD VILLAGE",
          "WAUREGAN",
      "POMFRET",
      "PUTNAM",
          "PUTNAM DISTRICT",
      "SCOTLAND",
      "STERLING",
          "ONECO",
      "THOMPSON",
          "NORTH GROSVENOR DALE",
          "QUINEBAUG",
      "WINDHAM",
          "NORTH WINDHAM",
          "SOUTH WINDHAM",
          "WILLIMANTIC",
          "WINDHAM CENTER",
      "WOODSTOCK",
          "SOUTH WOODSTOCK"

  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FARM", "FARMINGTON",
      "UNVL", "UNIONVILLE",
      "WLFD", "WALLINGFORD"
  });

}
