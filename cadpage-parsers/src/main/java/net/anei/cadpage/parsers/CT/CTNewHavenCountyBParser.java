package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewHavenCountyBParser extends FieldProgramParser {
  
  public CTNewHavenCountyBParser() {
    this("NORTH BRANFORD", "CT");
  }
  
  public CTNewHavenCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "ID ( SELECT/1 ( CALL PLACE/Z ADDR1/Z APT/Z CITY/Z ZIP " +
                        "| CALL ADDR1/Z APT/Z CITY/Z ZIP " +
                        "| CODE? CALL ADDR1 CITY ) " +
             "| CODE? CALL? PLACE/Z ADDR2 ) " + 
          "( MAP_X UNIT/Z DATETIME! | UNIT/Z DATETIME! | DATETIME! ) INFO/N+");
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
    return "FirePaging@hamdenfirefighters.org,paging@branfordfire.com,paging@easthavenfire.com,paging@easthavenpolice.com,paging@mail.nbpolicect.org,paging@nbpolicect.org,noreply@nexgenpss.com,pdpaging@farmington-ct.org,noreply@whpd.com";
  }

  private static final Pattern DELIM1 = Pattern.compile(" *\\| *");
  private static final Pattern DELIM2 = Pattern.compile(" */ *");
  private static final Pattern MARKER = Pattern.compile("(\\d{10}) +(?:(S\\d{2}) +)?");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +(\\d{6}) (\\d\\d:\\d\\d)(?:[ ,]|$)"); 
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile(" +\\d{6} [\\d:]+$| +\\d{1,6}$"); 
  private static final Pattern PRI_MARKER = Pattern.compile(" - PRI (\\d) - ");
  private static final Pattern ADDR_ST_MARKER = Pattern.compile("(.*) (\\d{5} .*)");
  private static final Pattern I_NN_HWY_PTN = Pattern.compile("\\b(I-?\\d+) +HWY\\b");
  private static final Pattern ADDR_END_MARKER = Pattern.compile("Apt ?#:|(?=(?:Prem )?Map -)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("\\S+(?: \\d+)?\\b");
  private static final Pattern MAP_PFX_PTN = Pattern.compile("(?: *(?:Prem )?Map -*)+", Pattern.CASE_INSENSITIVE);
  private static final Pattern MAP_PTN = Pattern.compile("(?:\\d{1,2}(?:[ ,+]+\\d{1,4})?(?:[- ]*[A-Z]{2} *\\d{1,3})?|[- ]*[A-Z]{2} *\\d{1,3}|(?:[CMT]-?\\d+(?:, [A-Z]\\d[A-Z])?\\b[ &]*)+)\\b *");
  private static final Pattern MAP_EXTRA_PTN = Pattern.compile("\\(Prem Map -*(.*?)\\)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) - (\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)");
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) - (.*)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    body = body.replace('\n', ' ');
    
    // See if this is one of the delimited field formats
    String[] flds = DELIM1.split(body);
    if (flds.length > 5) {
      setSelectValue("1");
      if (!parseFields(flds, data)) return false;
    }
    
    else {
      flds = DELIM2.split(body);
      if (flds.length > 5) {
        setSelectValue("2");
        if (!parseFields(flds, data)) return false;
      }
      
      else {
        Matcher match = MARKER.matcher(body);
        if (!match.lookingAt()) return false;
        setFieldList("ID SRC CODE CALL PRI ADDR APT MAP X CITY UNIT DATE TIME INFO");
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
        // by cleaning off priority marker and looking for a start address construct
        StartType st = StartType.START_CALL;
        match = PRI_MARKER.matcher(body);
        if (match.find()) {
          st = StartType.START_ADDR;
          data.strCall = body.substring(0,match.start()).trim();
          data.strPriority = match.group(1);
          body = body.substring(match.end()).trim();
        }
        else if ((match = ADDR_ST_MARKER.matcher(body)).matches()) {
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
              int pt = pad.lastIndexOf(')');
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
              int pt = field.indexOf("  ");
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
            parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
            if (data.strCity.length() > 0) field = getLeft();
          }
        }
        
        // Whatever is left becomes the unit
        data.strUnit = field.replaceAll("  +", " ");
        
        data.strCity = convertCodes(data.strCity, CITY_CODES);
      }
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
      int pt = data.strCall.indexOf(" - ");
      if (pt >= 0) {
        String part1 = data.strCall.substring(0, pt).trim();
        String part2 = data.strCall.substring(pt+3).trim();
        if (part1.equals(part2)) data.strCall = part1;
      }
    }

//    if (data.strCode.length() > 0) {
//      String call = CALL_CODES.getCodeDescription(data.strCode, true);
//      if (call != null) data.strCall = call;
//    }
    
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
      Matcher match = ADDRESS_ZIP_PTN.matcher(field);
      if (!match.matches()) return false;
      field = match.group(1).trim();
      String zip = match.group(2);
      
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      if (data.strCity.length() == 0) data.strCity = zip;
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
      data.strCross = field;
    }
    
    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("(\\d\\d[-/]\\d\\d[-/]\\d{4}) (\\d\\d:\\d\\d(?::\\d\\d)?)\\b.*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (!match.matches()) return false;;
      data.strDate = match.group(1).replace('-', '/');
      data.strTime = match.group(2);
      return true;
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

//  private static CodeTable CALL_CODES = new StandardCodeTable();

  private static final String[] MWORD_STREET_LIST = new String[]{
      "ALLINGS CROSSING",
      "ASBURY RIDGE",
      "ASHFORD CENTER",
      "AUTUMN RIDGE",
      "AVALON GATE",
      "BABCOCK HILL",
      "BAHRE CORNER",
      "BATTERSON PARK",
      "BEACH HILL",
      "BEACON HILL",
      "BEACON POINT",
      "BEAVER HEAD",
      "BEL AIRE",
      "BIDWELL FARM",
      "BIRCH KNOLL",
      "BLISS MEMORIAL",
      "BOOTH HILL",
      "BOSTON POST",
      "BOULDER RIDGE",
      "BREEZY HILL",
      "BRIAR HILL",
      "BUENA VISTA",
      "BUNKER HILL",
      "BURNT HILL",
      "BUSINESS PARK",
      "CANTON SPRINGS",
      "CAPTAIN THOMAS",
      "CEDAR HILL",
      "CEDAR KNOLLS",
      "CEDAR LAKE",
      "CEDAR SWAMP",
      "CENTURY HILLS",
      "CHERRY HILL",
      "CIDER MILL",
      "CLEAR LAKE",
      "COLD SPRING",
      "COLT HWY",
      "COMMERCE CENTER",
      "COPE FARMS",
      "COPPER BEECH",
      "COSEY BEACH",
      "DANIELS FARM",
      "DAYTON HILL",
      "DEER RUN",
      "DUNBAR HILL",
      "DUNNE WOOD",
      "EAST GATE",
      "EAST HILL",
      "EAST MAIN",
      "EAST SHORE",
      "ELM COMMONS",
      "FARM GLEN",
      "FARM RIVER",
      "FARM SPRINGS",
      "FARMINGTON CHASE",
      "FAWN HILL",
      "FIELD STONE",
      "FLANDERS RIVER",
      "FLAT ROCK",
      "FLYING POINT",
      "FOOTE HILL",
      "FOREST HILLS",
      "FOX HILL",
      "FOXON HILL",
      "FRESH MEADOW",
      "GAYLORD MOUNTAIN",
      "GEO WASHINGTON",
      "GEORGE WASHINGTON",
      "GLEN DEVON",
      "GLEN HAVEN",
      "GLEN RIDGE",
      "GREAT HILL",
      "GREAT MEADOW",
      "GREEN GARDEN",
      "GREEN GLEN",
      "GREEN HILL",
      "HALF ACRE",
      "HALF KING",
      "HANES HILL",
      "HANG DOG",
      "HANKS HILL",
      "HART RIDGE",
      "HAYCOCK POINT",
      "HEMLOCK NOTCH",
      "HICKORY HILL",
      "HIDDEN BROOK",
      "HIDDEN OAK",
      "HIGH HILL",
      "HIGH POINT",
      "HIGH RIDGE",
      "HIGH TOP",
      "HIGHWOOD CROSSING",
      "HILLSIDE VIEW",
      "HINMAN MEADOW",
      "HOOP POLE",
      "HOPE HILL",
      "HORSEBARN HILL",
      "HOTCHKISS GROVE",
      "HUNTER'S RIDGE",
      "HUNTERS CROSSING BARNES HILL",
      "HUNTINGTON OLD GREEN",
      "INDIAN HILL",
      "INDIAN NECK",
      "ISLAND VIEW",
      "JIM CALHOUN",
      "JOHNNYCAKE MTN",
      "JUNIPER POINT",
      "KATHERINE GAYLORD",
      "KATIE JOE",
      "KAYE VUE",
      "KILLAMS POINT",
      "KING HILL",
      "KINGS COLLEGE",
      "KNOB HILL",
      "KROL FARM",
      "LAKE GARDA",
      "LANES POND",
      "LEEDER HILL",
      "LEETES ISLAND",
      "LITTLE OAK",
      "LITTLEBROOK CROSSING",
      "LOCH NESS",
      "LONG HILL CROSS",
      "LONG HILL",
      "MALLARD BROOK",
      "MAPLE RIDGE",
      "MEADOW WOOD",
      "MILL POND HEIGHTS",
      "MILLER FARMS",
      "MOUNT CARMEL",
      "MOUNT PLEASANT",
      "MOUNTAIN SPRING",
      "MOUNTAIN TOP PASS",
      "MOUNTAIN TOP",
      "MOUNTAIN VIEW",
      "MT CARMEL",
      "NORTH ATWATER",
      "NORTH EAGLEVILLE",
      "NORTH EAGLVILLE",
      "NORTH HIGH",
      "NORTH HILLSIDE",
      "NORTH LAKE",
      "NORTH MAIN",
      "NORTH MOUNTAIN",
      "NORTH PETERS",
      "NORTH WINDHAM",
      "NOTCH HILL",
      "OAK HILL",
      "OAK HOLLOW",
      "OAK RIDGE",
      "ORCHARD HILL",
      "OXEN HILL",
      "PARK POND",
      "PARKER FARMS",
      "PARSONAGE HILL",
      "PAUL SPRING",
      "PAWSON PARKWAY",
      "PEAT MEADOW",
      "PINE HURST",
      "PINE ORCHARD",
      "PINE ROCK",
      "PINE TREE",
      "PINE WOOD",
      "POLLY DAN",
      "POND VIEW",
      "POPLAR HILL",
      "PORTAGE CROSSING",
      "PRATTLING POND",
      "PUNCH BROOK",
      "PUTTING GREEN",
      "QUARRY DOCK",
      "QUEEN'S PEAK",
      "RED OAK HILL",
      "ROCK PASTURE",
      "ROCKY LEDGE",
      "ROLLING WOOD",
      "SAGAMORE COVE",
      "SAW MILL",
      "SCHOOL HOUSE",
      "SCOTT SWAMP",
      "SEA HILL",
      "SECRET LAKE",
      "SHINGLE MILL",
      "SHORT BEACH",
      "SILAS DEANE",
      "SILVER SANDS",
      "SILVERMINE ACRES",
      "SIR THOMAS",
      "SKIFF ST EXT",
      "SOUNDVIEW VIEW",
      "SOUTH EAGLEVILLE",
      "SOUTH END",
      "SOUTH MAIN",
      "SOUTH MONTOWESE",
      "SOUTH QUAKER",
      "SOUTH RIDGE",
      "SOUTH STRONG",
      "SPENO RDG FRANCE",
      "SPICE BUSH",
      "SPRING COVE",
      "SPRING ROCK",
      "ST MONICA",
      "STONE HILL",
      "STONY CREEK",
      "STONY HILL",
      "SUNSET BEACH",
      "SUNSET HILL",
      "SUNSET MANOR",
      "SYBIL CREEK",
      "TALCOTT NOTCH",
      "THE MEWS CENTURY HILLS",
      "THIMBLE ISLAND",
      "TIMBER BROOK",
      "TOWERS LOOP",
      "TOWN FARM",
      "TOWN LINE",
      "TRAP FALLS",
      "TROUT BROOK",
      "TUNXIS MEAD",
      "TUNXIS VILLAGE",
      "TURTLE BAY",
      "TWIN LAKE",
      "TWIN LAKES",
      "TWO MILE",
      "VALLE VIEW",
      "VAN HORN",
      "VICTOR HILL",
      "WALDEN GREEN",
      "WARNER HILL",
      "WAVERLY PARK",
      "WESLEY HEIGHTS",
      "WEST CAMPUS",
      "WEST CHIPPEN HILL",
      "WEST CLARK",
      "WEST DISTRICT",
      "WEST GATE",
      "WEST MAIN",
      "WEST MOUNTAIN",
      "WEST SIDE",
      "WEST SLOPE",
      "WEST SPRING",
      "WEST WOODS",
      "WHITE HOLLOW",
      "WHITE PLAINS",
      "WINDMILL HILL",
      "WOLF PIT",
      "WOOD ACRES",
      "WOODCHUCK HILL",
      "WOODS HILL"

  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ABD PAIN/PROB P-1",
      "ABD PAIN/PROB P-2",
      "ABNORMAL BREATHING DIFFICULTY SPEAKING - ASTHMA",
      "ACCIDENT - MV INJ ALPHA",
      "ACCIDENT - MV INJ BRAVO",
      "ACCIDENT - MV INJ BRAVO RED",
      "ACCIDENT- MV INJ DELTA",
      "ACCIDENT MV W/INJURIES",
      "ACTIVATED FIRE ALARM",
      "AFA",
      "AFA - COMMERCIAL/INDUSTRIAL",
      "AFA DAY RESPONSE",
      "AFA -  MULTI-FAMILY RESIDENTIAL",
      "AFA NIGHT RESPONSE",
      "AFA - RESIDENTIAL",
      "AFA RESIDENTIAL",
      "ALARM - FIRE",
      "ALLERG/STING P-1",
      "ALPHA MEDICAL",
      "ALS EMS RESPONSE",
      "ASSAULT - NOT DNGRS",
      "AUTO ACC RESPONSE",
      "AUTOMATIC FIRE ALARM",
      "BEHAVORIAL",
      "BLS EMS RESPONSE",
      "BREATH PROB P-1",
      "BRUSH FIRE",
      "BRUSH FIRE TWIN",
      "BUILDING LOCKOUT",
      "CARBON MONOXIDE",
      "CARDIAC / RESP. ARREST",
      "CHARLIE MEDICAL",
      "CHARLIE MEDICAL TF1",
      "CHARLIE MEDICAL TF3",
      "CHARLIE MEDICAL TF4A",
      "CHEST PAIN CLAMMY - PRI. 1 -",
      "CHEST PAIN DIFFICULTY SPEAKING",
      "CHEST PAIN P-1",
      "CHEST PAIN  - PRIORITY 1 -",
      "CHEST PAIN, SOB - PRI. 1 -",
      "CO ALARM NO/UNK MEDICAL SX",
      "CODE P-1",
      "COVER/RELOCATE TO FIRE HQ",
      "DELTA MEDICAL TF1",
      "DIABETIC (ALERT)",
      "DIABETIC P-1",
      "DIFF. BREATHING",
      "EFD IN PROGRESS",
      "EMD IN PROGRESS",
      "EMS INCIDENT",
      "E-MUTUAL AID AMBULANCE REQUEST",
      "E-MUTUAL AID MEDIC INTERCEPT REQUEST",
      "EPD IN PROGRESS",
      "FAINTING >35 W/CARDIAC HX - PRI 1-",
      "FALL",
      "FALL (NOT ALERT)",
      "FALL P-2",
      "FALL, POSS. DANG. AREA- PRI 1 -",
      "FALL PUBLIC ASSIST(NO INJURY)",
      "FALL, UNKNOWN",
      "FIRE ALARM",
      "FIRE ALARM  00B12",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM COMMERCIAL/WATERFLOW/APARTMENTS",
      "FIRE ALARM-HIGH RISE/TARGET HAZARD",
      "FIRE ALARM-MULTIPLE DEVICES",
      "FIRE ALARM RESIDENTIAL (ONE & TWO FAMILY HOMES)",
      "FIRE APPLIANCE",
      "FIRE - BRUSH FIRE",
      "FIRE - CO ALARM",
      "FIRE - MV",
      "FIRE - OTHER WEST",
      "FIRE OUT REPORT W/ ODOR OF SMOKE",
      "FIRE - SMOKE/GAS INVEST INSIDE",
      "FIRE - SMOKE/GAS INVEST OUTSIDE",
      "FIRE STRUCTURE",
      "FIRE - VEHICLE",
      "F-STRUCTURE FIRE - RESIDENTIAL",
      "FUEL SPILL",
      "FUEL SPILL - INLAND WATER - OUTSIDE",
      "FUEL SPILL - MINOR - OUTSIDE",
      "FUEL SPILL (SMALL LESS THAN 50G)",
      "HAZMAT - ACTIVE GAS LEAK",
      "HAZMAT-CONTAINED- CHEMICAL",
      "HAZMAT - INVESTIGATION",
      "HEART PROB P-1",
      "HEAVY SMOKE INVESTIGATION OUTSIDE",
      "HEMORRHAGE THROUGH TUBES",
      "HEMORR/LAC P-1",
      "HEMORR/LAC P-2",
      "ILLEGAL BURNING",
      "INJURY P-1",
      "INTERFACILITY P2",
      "INVESTIGATE(OTHER NON-EMERGENT)",
      "INVESTIGATION",
      "LIFT ASSIST",
      "LIFT ASSIST (NO FALL)",
      "LOCK IN/OUT - BUILDING",
      "LONG FALL",
      "MARINE RESCUE",
      "MEDICAL ALARM ACTIVATION",
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
      "MEDICAL-C RESPONSE",
      "MEDICAL-D RESPONSE",
      "MEDICAL EMERGENCY",
      "MEDICAL MEDA",
      "MEDICAL MEDB",
      "MEDICAL MEDC",
      "MEDICAL MEDD",
      "MEDICAL MEDE",
      "MOTOR VEHICLE LOCK OUT",
      "MOUNTAIN / TECHNICAL RESCUE",
      "MUTUAL AID",
      "MUTUAL AID - FIRE",
      "MUTUAL AID INCIDENT MEDICAL",
      "MUTUAL AID - MEDICAL",
      "MUTUAL AID PARAMEDIC",
      "MUTUAL AID STANDBY",
      "MV ACCIDENT WEST",
      "MVA/ INJURIES REPORTED - RADIO",
      "MVA - INJURY",
      "MVA-ROLLOVER - RADIO",
      "MVA W/ INJURIES",
      "MVA W/INJURIES",
      "MVA WITH INJURIES",
      "MV CRASH-TRAFFIC CRASH (NO INJURY)",
      "MV CRASH-TRAFFIC CRASH (NO INJURY)-B",
      "MV CRASH-TRAFFIC CRASH (WITH INJURY)",
      "NATURAL GAS LEAK",
      "NATURAL / LP GAS - ODOR OUTSIDE",
      "NATURAL / LP GAS -  ODOR - RESIDENTIAL",
      "OD/INGEST P-1",
      "OMEGA MEDICAL",
      "OUTSIDE FIRE",
      "OUTSIDE FIRE - INVEST -UNKNOWN",
      "OVERDOSE UNCON.",
      "PERSON DOWN P-2",
      "PERSON STUCK IN ELEVATOR",
      "POSS HEART, SOB- PRI 1 -",
      "POST CHOKING - PRIORITY 2 -",
      "POST SEIZURE - PRI. 1 -",
      "PROPANE LEAK",
      "PUBLIC ASSIST",
      "PUBLIC ASSISTANCE FD",
      "PUBLIC SERVICE",
      "PUBLIC SERVICE (FIRE)",
      "RESET FIRE ALARM",
      "SEIZURE P-1",
      "SEIZURE(S) - PRI. 1 -",
      "SERVICE CALL - NON-EMERGENCY",
      "SICK CALL",
      "SICK CALL, AMS",
      "SICK CALL,  COND 2-11 NOT IDENTIFIED",
      "SICK CALL DIFF BREATHING",
      "SICK CALL - DIZZINESS",
      "SICK CALL, GEN. WEAKNESS",
      "SICK CALL IMMOBILITY",
      "SICK CALL, NOT ALERT",
      "SICK CALL NOT WELL / ILL",
      "SICK CALL OTHER PAIN",
      "SICK CALL P-1",
      "SICK CALL P-2",
      "SICK PERSON NAUSEA",
      "SICK PERSON NO PRIORITY SYMPTOMS",
      "SMOKE INVESTIGATION INSIDE",
      "SMOKE ODOR INVESTIGATION",
      "STILL",
      "STILL - ONE ENGINE",
      "STROKE",
      "STROKE - NOT ALERT",
      "STROKE/TIA P-1",
      "STRUCTURE FIRE",
      "Structure Fire COMMERCIAL/  hazmat",
      "STRUCTURE FIRE COMMERCIAL/INDUSTRIAL",
      "STRUCTURE FIRE RESIDENTIAL (MULTI)",
      "Structure Fire Residential (multi)   -Trapped (s)",
      "Structure Fire Residential (single)",
      "STRUCTURE FIRE RESIDENTIAL (SINGLE)",
      "TEST - TEST1234567890",
      "TRAFFIC/TRANS ACCID/ INJURIES",
      "TRANSFORM FIRE",
      "TRAUMATIC INJ., NOT DANG.",
      "UNCON. EFF. BREATHING",
      "UNCON/FAINTING NOT ALERT",
      "UNCON/FAINT P-1",
      "VEHICLE FIRE",
      "WATER CONDITION",
      "WATER RESCUE",
      "WELFARE CHECK",
      "WELFARE CHECK - FD",
      "WIRES DOWN",
      "WIRES DOWN/BURNING",
      "WIRES DOWN - CHECK FOR HAZARDS"
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
      
      // Fairfield County
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
      "UNIONVILLE",
      "WEST HARTFORD",
      "WETHERSFIELD",
      "WINDSOR",
      "WINDSOR LOCKS",
      "WINDSOR LOCKS EAST",
      
      // Middlesex County
      "CROMWELL",
      
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
