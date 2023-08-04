package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Carroll County, MD
 */
public class MDCarrollCountyAParser extends FieldProgramParser {

  public MDCarrollCountyAParser() {
    super("CARROLL COUNTY", "MD",
          "TIME CT:ADDR! BOX:BOX! DUE:UNIT!");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@c-msg.net,carrollalert@carroll911.mygbiz.com,alerts@carroll911.org";
  }

  private static final Pattern TIME_SEQ_TIME_PTN = Pattern.compile("(\\d\\d:\\d\\d)(CT:.*?) (?:(\\d{6,8}) )?(\\d\\d:\\d\\d)");
  private static final Pattern TRAIL_SEQ = Pattern.compile(" \\[\\d+\\]$");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Try to parse long format
    if (parseLongForm(subject, body, data)) return true;

    do {
      if (subject.equals("!")) break;

      if (subject.endsWith("|!")) {
        data.strSource = subject.substring(0,subject.length()-2);
        break;
      }

      int pt = body.indexOf(" / [!] ");
      if (pt >= 0) {
        data.strSource = body.substring(0,pt).trim();
        body = body.substring(pt+7).trim();
        break;
      }

      // Message signatures are now optional.  If we don't find one, keep on procesing
    } while (false);

    String[] subFlds = subject.split("|");
    if (subFlds.length == 2 && subFlds[0].equals("CMalert")) data.strSource = subFlds[1];

    String prefix = "";
    if (body.startsWith("RE-ALERT!\n")) {
      prefix = "RE-ALERT!";
      body = body.substring(10).trim();
    }

    int pt = body.indexOf("\nDO NOT RESPOND");
    if (pt >= 0) {
      prefix = append(prefix," ", "DO NOT RESPOND");
      body = body.substring(0,pt).trim();
    }

    Matcher match = TIME_SEQ_TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(2).trim();
      data.strCallId = getOptGroup(match.group(3));
      data.strTime = match.group(4);
    }
    else {
      match = TRAIL_SEQ.matcher(body);
      if (match.find()) body = body.substring(0,match.start()).trim();
    }

    if (!super.parseMsg(body, data)) return false;
    data.strCall = append(prefix, " - ", data.strCall);
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " ID TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }

  private static final Pattern MA_PTN = Pattern.compile("^(?:MA|MUTUAL AID ALARM) (?:\\*{3} *(?:BOX )?(?:[A-Z]{2} *)?([-\\d]+)|(?:BOX |(?!RT|US|ST)[A-Z]{2}[/ ]+)?(?:(\\d{1,2}-\\d{1,2}(?:-\\d{1,2})?))? +)?|^([A-Z]+) +(\\d+-\\d+) +");
  private static final Pattern MA_SEPARATOR_PTN = Pattern.compile(" +- +| *[/;,] *");
  private static final Pattern CHANNEL_PTN = Pattern.compile("(?:[ \\.]+(CP|TB|TG|FC) *| +)(\\d{1,2}(?:-?[A-Z])?|WEST)$");
  private static final Pattern BOX_PTN = Pattern.compile("\\d{1,2}-\\d{1,2}(?:-\\d{1,2})?");
  private static final Pattern BOX_PTN2 = Pattern.compile("(?: +BOX)? +(\\d{1,2}-\\d{1,2}(?:-\\d{1,2})?)$");
  private class MyAddressField extends Field {

    @Override
    public void parse(String fld, Data data) {

      fld = fld.replaceAll("/+ */+", "/");
      fld = stripFieldEnd(fld, "/");

      // The rules all change for mutual aid calls
      Matcher match = MA_PTN.matcher(fld);
      if (match.find()) {
        String maCall = match.group(3);
        if (maCall != null) {
          data.strBox = match.group(4);
        } else {
          maCall = "MA";
          data.strBox = match.group(1);
          if (data.strBox == null) data.strBox = match.group(2);
          if (data.strBox == null) data.strBox = "";
        }
        fld = fld.substring(match.end()).trim();

        // Sometimes, the address line is nicely split out with slashes, dashes, or semicolons
        // But first protectect L/Z short for Landing Zone
        // Also CP/TB whatever that may be
        String[] flds = MA_SEPARATOR_PTN.split(fld.replace(" L/Z", " L%Z").replace(" CP/TB", " CP%TB"));
        if (flds.length > 2) {
          parseAddress(flds[0], data);
          int sNdx = 1;
          if (isValidAddress(flds[1])) {
            if (data.strAddress.length() == 0 || !Character.isDigit(data.strAddress.charAt(0))) {
              data.strAddress = append(data.strAddress, " & ", flds[1]);
            } else {
              data.strCross = flds[1];
            }
            sNdx++;
          }

          int eNdx = flds.length-1;
          if (eNdx-sNdx > 0 && data.strChannel.length() == 0) {
            data.strChannel = flds[eNdx--];
          }
          if (data.strBox.length() == 0 && eNdx-sNdx > 0) {
            String box = flds[eNdx].trim();
            if (BOX_PTN.matcher(box).matches()) {
              data.strBox = box;
              eNdx--;
            }
          }
          data.strCall = append(maCall, " - ", flds[sNdx].replace("L%Z", "L/Z").replace("CP%TB", "CP/TB"));
          for (int j = sNdx+1; j <= eNdx; j++) {
            data.strCall = append(data.strCall, " / ", flds[j].replace("L%Z", "L/Z").replace("CP%TB", "CP/TB"));
          }
        }

        // and sometimes they do not :(
        else {

          // Check for trailing channel
          match = CHANNEL_PTN.matcher(fld);
          if (match.find()) {
            data.strChannel = append(getOptGroup(match.group(1)), " ", match.group(2));
            fld = fld.substring(0,match.start()).trim();
          }

          // And a trailing box number
          match = BOX_PTN2.matcher(fld);
          if (match.find()) {
            data.strBox = match.group(1);
            fld = fld.substring(0,match.start());

            // and an occasional trailing channel in front of the box number :(
            if (data.strChannel.length() == 0) {
              match = CHANNEL_PTN.matcher(fld);
              if (match.find()) {
                data.strChannel = append(getOptGroup(match.group(1)), " ", match.group(2));
                fld = fld.substring(0,match.start()).trim();
              }
            }
          }

          String call;
          int pt = fld.indexOf(';');
          if (pt >= 0) {
            call = fld.substring(pt+1).trim();
            fld = fld.substring(0,pt).trim();
            parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, fld, data);
          }

          else {
            parseAddress(StartType.START_PLACE, fld, data);
            call = getLeft();
          }
          call = stripFieldStart(call, "/");

          if (data.strPlace.endsWith("/")) {
            data.strAddress = append(data.strPlace.substring(0,data.strPlace.length()-1).trim(), " & ", data.strAddress);
            data.strPlace = "";
          }

          // Otherwise, just take the leftover stuff as the call description
          // If there isn't anything left over, grab the leading place field instead
          if (call.length() == 0) {
            call = data.strPlace;
            data.strPlace = "";
          }
          data.strCall = append("MA", " - ", call);
        }
      }

      // If normal (not mutual aid) call
      else {

        // First see if we recognize the call description
        StartType st = StartType.START_CALL;
        int flags = FLAG_START_FLD_REQ | FLAG_ANCHOR_END;
        CodeTable.Result res = CALL_TABLE.getResult(fld, true);
        if (res != null) {
          st = StartType.START_PLACE;
          flags = FLAG_ANCHOR_END;
          data.strCode = res.getCode();
          data.strCall = res.getDescription();
          if (data.strCode.equals(data.strCall)) data.strCode = "";
          fld = res.getRemainder();
        }

        parseOurAddress(st, flags, fld, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL BOX PLACE ADDR X APT CITY CH";
    }
  }

  // Box field behaves normally unless this is a mutual aid call
  // in which case it becomes a county code
  private static final Pattern BOX_CODE_PTN = Pattern.compile("([A-Z]{2})\\d+");
  private class MyBoxField extends BoxField {

    @Override
    public void parse(String fld, Data data) {
      if (data.strCall.startsWith("MA ") || data.strCall.equals("MA")) {
        String code = fld;
        Matcher match = BOX_CODE_PTN.matcher(fld);
        if (match.matches()) code = match.group(1);
        String[] tmp = convertCodes(code, COUNTY_CODES).split(",");
        data.strCity = tmp[0];
        if (tmp.length > 1) data.strState = tmp[1];
      }
      else {
        super.parse(fld, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "BOX CITY ST";
    }
  }

  private static final Pattern EVT_PRT_MARKER = Pattern.compile("C\\.A\\.D\\. +Event Listing For: (\\S+) +");
  private static final Pattern EVT_PRT_FND_CALL1_PTN = Pattern.compile(" HR  PR\n(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) +FND: +(.*?) {6,}");
  private static final Pattern EVT_PRT_RPT_CALL2_PTN = Pattern.compile("\n +RPT: +(.*?) +\n");
  private static final Pattern EVT_PRT_CODE_CALL_PTN = Pattern.compile("(\\d{4}) +(.*)");
  private static final Pattern EVT_PRT_LOCATION_PTN = Pattern.compile("\nLocation +C/A +USE +OPER\n(.*?) {6,}");
  private static final Pattern EVT_PRT_BOX_PTN = Pattern.compile("\\*\\*\\*(?:BOX)? *([-0-9]+)[ /]+(.*)");
  private static final Pattern EVT_PRT_APT_PTN = Pattern.compile("(.*?) *(?:APT|ROOM|LOT) +(\\S+)\\b *(.*?)");

  private static final Pattern MBREAK_PTN = Pattern.compile("\n{2,}");
  private static final Pattern PREFIX_PTN = Pattern.compile("(\\d\\d:\\d\\d) +");
  private static final Pattern TRIM_SPACE_PTN = Pattern.compile(" +\n *| *\n +");
  private static final Pattern CALL_ID_PTN = Pattern.compile("Call Type +([ /A-Z0-9]+?) +\\(([^)]+)\\) +Incident No(?: +(\\d+))?");
  private static final Pattern ADDR_PTN = Pattern.compile("Loc *\\b(.*?)");
  private static final Pattern BOX_ADDR_PTN = Pattern.compile("\\*{3}(?:BOX *)?(?:[A-Z]{2})? *([-\\d]+)[ /]+(.*)");
  private static final Pattern CROSS_MAP_PTN = Pattern.compile("(.*?) *(\\d+-[A-Z]\\d+)?");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {3,}");
  private static final Pattern NAME_PTN = Pattern.compile("Name *(.*?)");
  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("Address (.*) Phone (.*) Area *(.*)");
  private static final Pattern HOUSE_NBR_PTN = Pattern.compile("\\d+ .*");
  private static final Pattern LONG_ID_PTN = Pattern.compile("(?:INCIDENT UNIT HISTORY FOR:|ProQa Data for Incident:) *(\\d+)\\b|\\d\\d/\\d\\d/\\d\\d +");
  private static final Pattern UNIT_PTN = Pattern.compile("\nUnits *(.*)(?=\n|$)");

  private boolean parseLongForm(String subject, String body, Data data) {

    // Process event print format
    if (subject.equals("Event Print")) {
      data.msgType = MsgType.RUN_REPORT;
      setFieldList("ID DATE TIME CODE CALL BOX ADDR APT CITY PLACE INFO");

      Matcher match = EVT_PRT_MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      data.strCallId = match.group(1);
      body = body.substring(match.end());

      Parser p = new Parser(body);
      match = p.getMatcher(EVT_PRT_FND_CALL1_PTN);
      if (match == null) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      String call = match.group(3);

      match = p.getMatcher(EVT_PRT_RPT_CALL2_PTN);
      if (match == null) return false;
      if (call.length() == 0) call = match.group(1);
      match = EVT_PRT_CODE_CALL_PTN.matcher(call);
      if (match.matches()) {
        data.strCode = match.group(1);
        call = match.group(2);
      }
      data.strCall = call;

      match = p.getMatcher(EVT_PRT_LOCATION_PTN);
      if (match == null) return false;
      String addr = match.group(1);
      match = EVT_PRT_BOX_PTN.matcher(addr);
      if (match.matches()) {
        data.strBox = match.group(1);
        addr = match.group(2);
      }
      String apt = "";
      int pt = addr.indexOf(',');
      if (pt >= 0) {
        String place = addr.substring(pt+1).trim();
        addr = addr.substring(0,pt).trim();
        match = EVT_PRT_APT_PTN.matcher(place);
        if (match.matches()) {
          addr = append(match.group(1), " ", match.group(3));
          apt = match.group(2);
        }
      }
      parseAddress(addr, data);
      data.strApt = append(apt, "-", apt);

      p.get("\n \n");
      String info = p.get();
      if (!info.startsWith("Caller Information")) return false;
      data.strSupp = info;
      return true;
    }


    // Check subject and message prefix
    if (!subject.equals("CAD") && !subject.equals("Dispatch Rip/Run")) return false;
    Matcher match = PREFIX_PTN.matcher(body);
    if (!match.lookingAt()) return false;

    setFieldList("TIME CODE CALL ID ADDR APT CITY X MAP NAME PHONE PLACE INFO UNIT");

    data.strTime = match.group(1);
    body = body.substring(match.end());

    // remove extraneous blanks
    body = TRIM_SPACE_PTN.matcher(body).replaceAll("\n");

    // and message trailer
    body = stripFieldEnd(body, "<< END OF MESSAGE >>");

    // Parse dispatch alert
    if (body.startsWith("DISPATCH INFO\n\n")) {
      int pt = body.indexOf("\n\n*** Premise Info ***");
      if (pt >= 0) body = body.substring(0,pt).trim();
      body = body.substring(15).trim();
      body = MBREAK_PTN.matcher(body).replaceAll("\n");
      Parser p = new Parser(body);
      match = CALL_ID_PTN.matcher(p.getLine());
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
      data.strCallId = getOptGroup(match.group(3));

      match = ADDR_PTN.matcher(p.getLine());
      if (!match.matches()) return false;
      String addr = match.group(1).trim();
      match = BOX_ADDR_PTN.matcher(addr);
      if (match.matches()) {
        data.strBox = match.group(1);
        addr = match.group(2);
      }
      parseOurAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);

      String line = p.getLine();
      if (!line.startsWith("Name")) {
        match = CROSS_MAP_PTN.matcher(line);
        if (!match.matches()) return false;
        String cross = match.group(1);
        cross = MBLANK_PTN.matcher(cross).replaceAll(" / ");
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
        data.strMap = getOptGroup(match.group(2));

        line = p.getLine();
      }

      match = NAME_PTN.matcher(line);
      if (!match.matches()) return false;
      data.strName = cleanWirelessCarrier(match.group(1).trim());

      match = PLACE_PHONE_PTN.matcher(p.getLine());
      if (!match.matches()) return false;
      // Places that look like street addresses are probably
      // cell tower location that we can ignore
      String place = match.group(1).trim();
      if (!HOUSE_NBR_PTN.matcher(place).matches()) {
        if (!data.strPlace.equals(place)) {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      }
      String phone = match.group(2).trim();
      if (phone.length() > 1) {
        String ac = match.group(3).trim();
        if (ac.length() > 0) phone = '(' + ac + ") " + phone;
        data.strPhone = phone;
      }

      String info = stripFieldStart(p.get(), "Remarks");
      match = UNIT_PTN.matcher(info);
      if (match.find()) {
        data.strUnit = match.group(1).trim();
        info = info.substring(0,match.start()) + info.substring(match.end());
      }
      data.strSupp = info;
      return true;
    }

    // Try to find a call ID
    match = LONG_ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = getOptGroup(match.group(1));

    // Set the message type
    data.msgType = (body.startsWith("ProQa Data for Incident:") ? MsgType.GEN_ALERT : MsgType.RUN_REPORT);

    // Everything goes into info
    data.strSupp = body;
    return true;
  }

  private static final Pattern BOX1_PFX_PTN = Pattern.compile("([A-Z]C), *");
  private static final Pattern BOX2_PFX_PTN = Pattern.compile("(\\d{1,2}-\\d{1,2})[ /]+");
  private static final Pattern APT_PTN = Pattern.compile("(?:\\bAPT\\b|#) *([^ ]+)$", Pattern.CASE_INSENSITIVE);

  private void parseOurAddress(StartType st, int flags, String fld, Data data) {

    // Next check if the last token is a recognized city and
    // strip it off if it is, otherwise it messes up the following logic
    int pt = fld.lastIndexOf(" City");
    if (pt >= 0) {
      data.strCity = convertCodes(fld.substring(pt+5).trim(), CITY_CODES);
      fld = fld.substring(0,pt).trim();
    } else {
      Parser p = new Parser(fld);
      String city = CITY_CODES.getProperty(p.getLast(' '));
      if (city != null) {
        data.strCity = city;
        fld = p.get();
      }
    }

    pt = fld.lastIndexOf(" Apt");
    if (pt >= 0) {
      data.strApt = fld.substring(pt+4).trim();
      fld = fld.substring(0,pt).trim();
    }

    // Again, the rules change when this is a mutual aid alarm
    if (data.strCall.equals("MUTUAL AID ALARM")) {
      Matcher match = BOX1_PFX_PTN.matcher(fld);
      if (match.lookingAt()) {
        data.strBox = match.group(1);
        fld = fld.substring(match.end());
        if (data.strCity.length() == 0) {
          String city = COUNTY_CODES.getProperty(data.strBox);
          if (city !=  null) {
            String[] tmp = city.split(",");
            data.strCity = tmp[0];
            if (tmp.length > 1) data.strState = tmp[1];
          }
        }
      }

      match = BOX2_PFX_PTN.matcher(fld);
      if (match.lookingAt()) {
        data.strBox = append(data.strBox, " ", match.group(1));
        fld = fld.substring(match.end());
      }

      parseAddress(StartType.START_ADDR, FLAG_NO_CITY, fld, data);
      String call = getLeft();
      call = stripFieldStart(call, "/");
      call = stripFieldStart(call, "/");
      data.strCall = append(data.strCall, " - ", call);
      return;
    }

    // Rest of address could include a place name separated by a ; or @
    // Unfortunately, the two fields might be in either order :(
    // Worse, there could be 3 or  more :(  In which case we only
    // check the first two and force the last one into a place field
    // And the place name might contain an apartment
    String[] flds = fld.split(" *[;@,] *", 3);
    if (flds.length == 1) {
      parseAddress(st, flags, fld, data);
      if (data.strPlace.endsWith("/")) {
        data.strAddress =  append(data.strPlace.substring(0,data.strPlace.length()-1).trim(), " & ", data.strAddress);
        data.strPlace = "";
      }
    }
    else {
      String fld1 = flds[0];
      String fld2 = flds[1];
      String savePlace = flds.length > 2 ? flds[2] : "";
      Result res1 = parseAddress(st, flags | FLAG_CHECK_STATUS, fld1);
      Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, fld2);
      if (res2.getStatus() > res1.getStatus()) {
        res1 = res2;
        fld2 = fld1;
      }
      res1.getData(data);
      if (data.strPlace.endsWith("/")) {
        data.strAddress =  append(data.strPlace.substring(0,data.strPlace.length()-1).trim(), " & ", data.strAddress);
        data.strPlace = "";
      }
      Matcher match = APT_PTN.matcher(fld2);
      if (match.find()) {
        data.strApt = match.group(1);
        fld2 = fld2.substring(0,match.start()).trim();
      }
      data.strPlace = append(data.strPlace, " - ", fld2);

      if (savePlace.length() > 0) {
        match = APT_PTN.matcher(savePlace);
        if (match.find()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          savePlace =  savePlace.substring(0,match.start()).trim();
        }
        data.strPlace = append(data.strPlace, " - ", savePlace);
      }
    }
  }

  @Override
  public String adjustMapAddress(String address) {
    // Other location seem OK with decimal street addresses, but not here
    return DECIMAL_PTN.matcher(address).replaceFirst("$1");
  }
  private static final Pattern DECIMAL_PTN = Pattern.compile("^(\\d+)\\.\\d+");

  private static final String[] MWORD_STREET_LIST = new String[]{
    "AGRICULTURAL CENTER",
    "ARTHUR SHIPLEY",
    "BACHMANS VALLEY",
    "BALTIMORE NATIONAL",
    "BARK HILL",
    "BEAR BRANCH",
    "BERT KOONTZ",
    "BIRD VIEW",
    "BLACK ANKLE",
    "BLACK ROCK",
    "BOLLINGER MILL",
    "BRASS EAGLE",
    "BRIDGE FIELD",
    "BUCHER JOHN",
    "BUNKER HILL",
    "CARROLL HIGHLANDS",
    "CHERRY TREE",
    "CHISELED STONE",
    "CLEAR HILL",
    "CLEAR RIDGE",
    "CLOVER LEAF",
    "CLOVER MEADOW",
    "COON CLUB",
    "CORPORATE CENTER",
    "CROUSE MILL",
    "DARK HOLLOW",
    "DAVE RILL",
    "DEER PARK",
    "DOCTOR STITELY",
    "EAST CHERRY HILL",
    "FLINT ROCK",
    "FOX CHASE",
    "GETTYSBURG VILLAGE",
    "GILLIS FALLS",
    "GIST FARM",
    "GRAVE RUN",
    "GREEN MEADOW",
    "GREEN MILL",
    "GREEN VALLEY",
    "GRIST STONE",
    "HEALTH CTR",
    "HIGH ACRE",
    "HIGH EARLS",
    "HIGHLAND VIEW",
    "HOOPERS DELIGHT",
    "HUGHES SHOP",
    "INDIAN VALLEY",
    "IRVING RUBY",
    "JOHN HYDE",
    "KATE WAGNER",
    "KAYS MILL",
    "KIMBERLY JAMES",
    "KINGS FOREST",
    "KLEE MILL",
    "LE MANS",
    "LEES MILL",
    "LONDON BRIDGE",
    "LONGLEAF PINE",
    "LORI JEAN",
    "MALL RING",
    "MAPLE GROVE",
    "MEADOW CREEK",
    "MEADOW FIELD",
    "MILLERS STATION",
    "MISTY MEADOW",
    "MOUNTAIN LAUREL",
    "MT CARMEL",
    "MT ZION",
    "NORTH WOODS",
    "OAK ORCHARD",
    "OAK VIEW",
    "OTTERDALE MILL",
    "PINCH VALLEY",
    "PINE VIEW",
    "PINE WOOD",
    "PINEY RIDGE",
    "PINYON PINE",
    "PLEASANT VALLEY",
    "POPES CREEK",
    "SAINT MARK",
    "SAINT PAUL",
    "SALEM BOTTOM",
    "SAMS CREEK",
    "SAW MILL",
    "SCOTCH HEATHER",
    "SILVER MEADOW",
    "SILVER RUN VALLEY",
    "SOUTH CARROLL PARK",
    "ST MARK",
    "ST PAUL",
    "STEEPLE CHASE",
    "STONE HOUSE VILLAGE",
    "TALBOT RUN",
    "TIMBER BRANCH",
    "TIMBER SHED",
    "UNION BRIDGE",
    "VALLEY VIEW ORCHARD",
    "VELVET RUN",
    "VILLA HILL",
    "VILLAGE OAKS",
    "WAKEFIELD VALLEY",
    "WEST FALLS",
    "WHITE ROCK",
    "WOLF HILL",
    "ZEIGLERS OUTLET"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AIRY", "MT AIRY",
      "COOK", "COOKSVILLE",
      "DETO", "DETOUR",
      "FINK", "FINKSBURG",
      "HAMP", "HAMPSTEAD",
      "KEYM", "KEYMAR",
      "LINE", "LINEBORO",
      "LINW", "LINWOOD",
      "MANC", "MANCHESTER",
      "MARR", "MARRIOTSVILLE",
      "MIDD", "MIDDLEBURG",
      "MILL", "MILLERS",
      "NWIN", "NEW WINDSOR",
      "RAND", "RANDALLSTOWN",
      "REIS", "REISTERSTOWN",
      "SYKE", "SYKESVILLE",
      "TANE", "TANEYTOWN",
      "UNBR", "UNION BRIDGE",
      "UPCO", "UPPERCO",
      "WEST", "WESTMINSTER",
      "WOOD", "WOODBINE"
  });

  // Mutual aid count abbreviations
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "YC", "YORK COUNTY,PA",
      "BC", "BALTIMORE COUNTY",
      "HC", "HOWARD COUNTY",
      "FC", "FREDERICK COUNTY",
      "AC", "ADAMS COUNTY,PA",
      "MC", "MONTGOMERY COUNTY"
  });

  @Override
  public boolean checkCall(String call) {
    call = stripFieldStart(call, "RE-ALERT! - ");
    call = stripFieldStart(call, "RE-ALERT! ");
    call = stripFieldStart(call, "DO NOT RESPOND - ");
    if (call.startsWith("MA - ") || call.equals("MA")) return true;
    if (call.startsWith("MUTUAL AID ALARM")) return true;
    String chkCall = CALL_TABLE.getCodeDescription(call);
    return chkCall != null && chkCall.equals(call);
  }

  private static final CodeTable CALL_TABLE = new CodeTable();
  static {
    String key = null;
    for (String value : new String[]{
        "AAM",                 "AUTOMATIC MED ALARM",
        "ABDOM",               "ABDOMINAL PAIN/BLS",
        "ABDOMALS",            "ABDOMINAL PAIN/ALS",
        "AIR1L",               "LG PLANE INCOMING",
        "AIR1S",               "SM PLANE INCOMING",
        "AIR2L",               "LG PLANE CRASH/STRUC",
        "AIR2S",               "SM PLANE CRASH/STRUC",
        "AIR3L",               "LG PLANE CRASH",
        "AIR3S",               "SM PLANE CRASH",
        "ALARM",               "ALARM",
        "ALARM 1",             "RES FIRE ALARM",
        "ALARM 2",             "COMM FIRE ALARM",
        "ALLER",               "ALLER REACTION/ALS",
        "ALLERALS",            "ALLER REACTION/ALS",
        "ALLERBLS",            "ALLER REACTION/BLS",
        "ALS",                 "ALS MEDICAL CALL",
        "ALS+",                "ALS W/SUPPORT",
        "AMB TRAN",            "TRAN AMBULANCE TRANSFER",
        "AMB TRANS",           "TRAN AMBULANCE TRANSFER",
        "APPL",                "APPLIANCE FIRE",
        "ASTH",                "ASTHMA ATTACK",
        "ATR",                 "ATR RESCUE",
        "ATR/HM",              "ATR RESCUE W/HAZMAT",
        "ATROL",               "OVERLAND ATR RESCUE",
        "ATV",                 "ATV ACCIDENT",
        "BACK",                "BACK PAIN",
        "BACKALS",             "BACK PAIN/ALS",
        "BACKBLS",             "BACK PAIN/BLS",
        "BF",                  "BUILDING FIRE",
        "BF RESC",             "BLDG FIRE W/RESCUE",
        "BF/HM",               "BLDG FIRE W/HAZMAT",
        "BIO2",                "OPENED ITEM WITH RELEASE / NO DISTRESS",
        "BIO3",                "SUSPICIOUS ITEM WITH EXPOSURE",
        "BIO4",                "SUSPICIOUS PACKAGE W/PERSONS DOWN",
        "BIO5",                "EXPLOSION OR EXPLOSIVE DEVICES",
        "BLS",                 "BLS MEDICAL CALL",
        "BLSC",                "NON EMER BLS CALL",
        "BOX",                 "BOX ALARM",
        "BOX/HLH",             "BLDG/HIGH LIFE HAZ",
        "BOX/HLHZ",            "BLDG/HIGH LIFE HZ/HM",
        "BOX/HM",              "BOX ALARM W/HAZMAT",
        "BRUSH",               "BRUSH FIRE",
        "BURN",                "BURN INJURY",
        "BURNALS",             "BURN INJURY/ALS",
        "BURNBLS",             "BURN INJURY/BLS",
        "CA",                  "CARDIAC ARREST",
        "CARD",                "CARDIAC PATIENT",
        "CARDIAC",             "CARDIAC PATIENT",
        "CARDALS",             "CARDIAC PATIENT/ALS",
        "CARDBLS",             "CARDIAC PATIENT/BLS",
        "CHEST",               "CHEST PAIN",
        "CHESTALS",            "CHEST PAIN/ALS",
        "CHESTBLS",            "CHEST PAIN/BLS",
        "CHIM",                "CHIMNEY FIRE",
        "CHOKE" ,              "CHOKING",
        "CHOKEALS",            "CHOKING EPISODE/ALS",
        "CHOKEBLS",            "CHOKING EPISODE/BLS",
        "CO",                  "CO DET ACTIVATION",
        "CO 1",                "CO DET ACTIVATION",
        "CO 2",                "CO DET ACTIVATION",
        "CO 3",                "CO DET ACTIVATION",
        "CO 4",                "CO DET ACTIVATION",
        "COBLS",               "CO POISONING",
        "COPOIS",              "CARBON MONOXIDE POISONING",
        "COLD",                "COLD EXPOSURE",
        "COLDALS",             "COLD EXPOSURE",
        "COLL",                "BUILDING COLLAPSE",
        "CONST",               "CONSTRUCTION / INDUSTRIAL ACCIDENT",
        "COS",                 "CO W/SICK PERSON",
        "DIAB",                "DIABETIC/ALS",
        "DIABBLS",             "DIABETIC/BLS",
        "DLOC",                "DECREASED LOC/ALS",
        "DOA",                 "DOA",
        "DROWN",               "DROWNING",
        "DROWNALS",            "NEAR DROWNING",
        "DROWNBLS",            "NEAR DROWNING",
        "ELEC",                "ELECTRICAL BURN",
        "ELEV",                "ELEVATOR RESC W/INJ",
        "ELEV/SC",             "ELEVATOR RESC NO INJ",
        "ET",                  "EMERGENCY TRANSFER",
        "EYE",                 "EYE INJURY",
        "EYEALS",              "EYE INJURY/ALS",
        "EYEBLS",              "EYE INJURY/BLS",
        "FARM",                "FARM MACHINERY FIRE",
        "FIELD",               "FIELD FIRE",
        "FIRE INVOL EXPOSURES","FIRE INVOL EXPOSURES",
        "FIREOUT",             "FIRE REPORTED OUT",
        "FUELODR",             "ODOR OF FUEL",
        "GAS LINE",            "GAS LINE STRUCK",
        "GAS/OUT",             "OUTSIDE GAS LEAK",
        "GASC",                "GAS LEAK IN COMMERCI",
        "HANG",                "HANGING",
        "HAZMAT",              "HAZMAT INCIDENT (SPECIFY)",
        "HEADACHE",            "HEADACHE",
        "HEAT",                "HEAT EMERGENCY",
        "HEATALS",             "HEAT EMERGENCY/ALS",
        "HEATBLS",             "HEAT EMERGENCY/BLS",
        "HEM",                 "HEMORRHAGE/BLS",
        "HEMALS",              "HEMORRHAGE/ALS",
        "HEMIVT",              "HEMORRHAGE",
        "HF",                  "HOUSE FIRE",
        "HM",                  "HAZMAT ALARM",
        "ILLEG",               "ILLEGAL OPEN BURNING",
        "INHAL",               "INHALATION EMERGENCY",
        "INHALBLS",            "INHALATION EMERGENCY",
        "INJ",                 "INJ PERSON/BLS",
        "INJ PER/FALL-BLS",    "INJ PER/FALL-BLS",
        "INJ PERSON/ALS",      "INJURED PERSON/ALS",
        "INJALS",              "INJURED PERSON (SPECIFY NATURE)",
        "INJFALL",             "INJ PER/FALL/BLS",
        "INJAS",               "INJ PER/ASSAULT/BLS",
        "INJASALS",            "INJURED PERSON FROM ASSAULT (ALS)",
        "INJBLS",              "INJ PERSON/BLS ",
        "INVEST",              "INVESTIGATION",
        "IVT",                 "IVT MEDICAL ALARM",
        "LARGE TRUCK FIRE",    "LARGE TRUCK FIRE",
        "LO/E",                "EMERGENCY LOCKOUT",
        "LOCAL",               "LOCAL ALARM",
        "MA",                  "MUTUAL AID ALARM",
        "MERCLG",              "MERCURY RELEASE / LARGE",
        "MERCSM",              "MERCURY RELEASE / SMALL",
        "MISC",                "MISCELLANEOUS FIRE - SPECIFY",
        "MO",                  "MENTAL PT/BLS",
        "MOV",                 "VIOL MENTAL PT/BLS",
        "NOSE",                "NOSEBLEED",
        "OB",                  "OBSTETRIC PATIENT",
        "OBALS",               "OBSTETRIC PATIENT ALS",
        "ODOR OF SMOKE",       "ODOR OF SMOKE",
        "OVERDOSE",            "OVERDOSE/ALS",
        "ODBLS",               "OVERDOSE/BLS",
        "ODORSICK",            "ODOR WITH SICK SUB",
        "OUTSIDE",             "OUTSIDE FIRE",
        "PA",                  "PATIENT ASSIST",
        "PATIENT",             "PATIENT ASSIST",
        "PED",                 "PEDESTRIAN STRUCK",
        "PIAMB",               "INJURY FROM VEHICLE ACCIDENT",
        "POIS",                "POISONING",
        "POISBLS",             "POISONING BLS",
        "PROQA DEFAULT",       "PROQA DEFAULT",
        "REKIN",               "REKINDLE",
        "RES FIRE ALARM",      "RES FIRE ALARM",
        "RESC",                "RESCUE ALARM",
        "RESCONF",             "CONFINED SPACE RESCUE",
        "RESHA",               "HIGH ANGLE RESCUE",
        "RESMI/ALS",           "SPECIFY NATURE (ALS RESPONSE)",
        "RESP",                "TROUBLE BREATHING",
        "RESTR",               "TRENCH RESCUE",
        "RESWN",               "INLAND WATER RESCUE",
        "RESWR",               "SWIFT WATER RESCUE",
        "ROUT",                "ROUTINE TRANSPORT",
        "SC/CO",               "CO DETECTOR ACTIVATION",
        "SC/ELEV",             "STUCK ELEVATOR",
        "SC/FLOOD",            "FLOODING CONDITION",
        "SC/HELI",             "HELICOPTER LANDING ZONE",
        "SC/LOCK",             "LOCKOUT",
        "SC/LOCKV",            "SUBJECT LOCKED IN A VEHICLE",
        "SC/WASH",             "WASHDOWN",
        "SEIZ",                "SEIZURE",
        "SEIZALS",             "SEIZURE/ALS",
        "SERVICE",             "SERVICE CALL",
        "SHOOT",               "SHOOTING",
        "SHOOTA",              "ACCIDENTAL SHOOTING",
        "SHOOTBLS",            "SHOOTING BLS",
        "SHOOTS",              "SELF INFLICTED SHOOTING",
        "SICK",                "SICK PERSON/BLS",
        "SICKALS",             "SICK PERSON/ALS",
        "SICKBLS",             "SICK PERSON/BLS",
        "SICK PERSON NON-EMER","SICK PERSON NON-EMER",
        "SNAKE",               "SNAKE BITE",
        "SPILL",               "HAZMAT ALARM - FUEL SPILL",
        "SPILLLM",             "LG FUEL SPILL",
        "SPILLSM",             "SM FUEL SPILL",
        "STAB",                "STABBING",
        "STABBLS",             "STABBING BLS",
        "STABS",               "SELF INFLICTED - KNIFE",
        "STROKE",              "STROKE",
        "STROKALS",            "STROKE/ALS",
        "STRUC",               "STRUCTURE ALARM",
        "SYNCO",               "SYNCOPAL EPISODE-ALS",
        "SYNCOALS",            "SYNCOPAL EPISODE ALS",
        "SYNCOBLS",            "SYNCOPAL EPISODE BLS",
        "TANKER",              "LG FUEL LOAD VEHICLE",
        "TRAIN AC",            "TRAIN ACCIDENT",
        "TRAIN FI",            "TRAIN FIRE",
        "TRANS",               "TRANSFER",
        "TRASH",               "TRASH FIRE",
        "TRUCKLG",             "LARGE TRUCK FIRE",
        "UC",                  "UNCONSCIOUS SUBJECT",
        "UNCDIAB",             "UNCONSCIOUS DIABETIC",
        "UNCOD",               "UNCONSCIOUS OVERDOSE",
        "UNK",                 "UNKNOWN MEDICAL EMERGENCY",
        "UP",                  "UNCONSCIOUS PERSON",
        "VC",                  "VEHICLE COLLISION",
        "VC/BIKE",             "BICYCLE ACCIDENT",
        "VC/MOT",              "MOTORCYCLE ACCIDENT",
        "VC BUS",              "BUS ACCIDENT",
        "VC INV BICYCLE",      "VEHICLE COLLISION INV BICYCLE",
        "VC RESCUE",           "VEHICLE COLLISION RESCUE",
        "VC SERIOUS",          "VEHICLE COLLISION SERIOUS",
        "VCMOT",               "VC INV MOTORCYCLE",
        "VCBUS",               "BUS ACCIDENT",
        "VCR",                 "VC RESCUE",
        "VCR/HM",              "VC W/HM",
        "VCS",                 "VEHICLE COLLISION SERIOUS",
        "VEH",                 "VEHICLE FIRE",
        "VEHICLE",             "VEHICLE COLLISION",
        "WIRES",               "WIRES DOWN",
        "WOODS",               "WOODS FIRE" }) {
      if (key == null) {
        key = value;
      } else {
        CALL_TABLE.put(key, value);
        CALL_TABLE.put(value, value);
        key = null;
      }
    }

    for (String value : new String[]{
        "ABDOMINAL PAIN-ALS",
        "ABDOMINAL PAIN-BLS",
        "ALLER REACTION-ALS",
        "ALLER REACTION-BLS",
        "ASTHMA ATTACK-ALS",
        "ASTHMA ATTACK-BLS",
        "BACK PAIN-ALS",
        "BACK PAIN-BLS",
        "BLDG FIRE",
        "CARDIAC PATIENT-ALS",
        "CARDIAC PATIENT-BLS",
        "CHEST PAIN-ALS",
        "CHEST PAIN-BLS",
        "DECREASED LOC-ALS",
        "DECREASED LOC-BLS",
        "DIABETIC-ALS",
        "DIABETIC-BLS",
        "DOA-ALS",
        "ELEVATOR RESC/NO INJ",
        "EMERGENCY LOCK OUT",
        "GAS LEAK IN RESIDENC",
        "HANGING-ALS",
        "HEMORRHAGE-ALS",
        "HEMORRHAGE-BLS",
        "INJ PER/ASSAULT-ALS",
        "INJ PER/ASSAULT-BLS",
        "INJ PERSON-ALS",
        "INJ PERSON-BLS",
        "MEDICAL PROQA DEFAULT",
        "MENTAL PT-ALS",
        "MENTAL PT-BLS",
        "OVERDOSE-ALS",
        "OVERDOSE-BLS",
        "SEIZURE-ALS",
        "SEIZURE-BLS",
        "SELF INFL STAB/ALS",
        "SELF INFL STAB/BLS",
        "SICK PERSON-ALS",
        "SICK PERSON-BLS",
        "STROKE-ALS",
        "STROKE-BLS",
        "SYNCOPAL EPISODE-ALS",
        "SYNCOPAL EPISODE-BLS",
        "TEST FIRE",
        "UNCONSCIOUS DIABETIC-ALS",
        "UNCONSCIOUS DIABETIC-BLS",
        "UNKNOWN NATURE ALS",
        "UNKNOWN NATURE BLS"
    }) {
      CALL_TABLE.put(value, value);
    }
  };
}
