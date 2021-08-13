package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Collin County, TX
 */
public class TXCollinCountyAParser extends FieldProgramParser {

  private static final Pattern MASTER1 = Pattern.compile("CFS(#)? (\\d{8}) +(.*)");
  private static final Pattern MSG_DISPATCH_PTN = Pattern.compile("Message [Ff]rom Dispatch ");
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile("(?:(?:\\[[^\\[\\]]*)?\\{[^\\{\\}]*?\\}?|\\[SENT: [:\\d]+\\])$");

  public TXCollinCountyAParser() {
    this("COLLIN COUNTY", "TX");
  }

  protected TXCollinCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/2 ID CALL ADDRCITY X GRID! UNITS:UNIT! CFS_RMK:INFO " +
          "| MASH! UNITS:UNIT ( St_Rmk:MAP! Grid_Map:MAP/L? Rmk1:INFO/N+ http:URL " +
                             "| ST_RMK:INFO/N CFS_RMK:INFO/N ) " +
          ") END");
    setupCallList(CALL_LIST);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "ccsodispatch@co.collin.tx.us,DispatchSMS@coppelltx.gov,ics.gateway@wylietexas.gov,wyliefiredispatch@gmail.com,CADPaging-NOREPLY@flower-mound.com,prosperdispatch@gmail.com,@outlook.com,icspage@murphytx.org,InfoRad@co.walker.tx.us,prosperdispatch@prospertx.gov";
  }


  @Override
  public String getAliasCode() {
    return "TXCollinCountyA";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean noParseSubjectFollow() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\nConfidentiality notice: ");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Rule out TXCollinCountyC alerts
    if (body.contains("\n")) return false;

    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      String delim = match.group(1) == null ? "  +" : "// +";
      data.strCallId = match.group(2);
      body = match.group(3);
      String flds[] = body.split(delim);
      if (flds.length > 1) {
        setFieldList("ID CALL ADDR APT INFO");
        flds[0] = flds[0].trim();
        flds[1] = flds[1].trim();
        int s1 = checkAddress(flds[0]);
        int s2 = checkAddress(flds[1]);
        if (s1 > 0 || s2 > 0) {
          if (s2 > s1) {
            parseAddress(flds[1], data);
            data.strCall = flds[0];
          } else {
            parseAddress(flds[0], data);
            data.strCall = flds[1];
          }

          for (int ndx = 2; ndx<flds.length; ndx++) {
            data.strSupp = append(data.strSupp, "\n", flds[ndx]);
          }
          return true;
        }
      }

      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }

    String alert = null;
    match = MSG_DISPATCH_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
      alert = body;
    }

    // Remove trailing ID
    match = TRAIL_JUNK_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    body = body.replace("CFS RMK ", "CFS RMK: ");

    if (subject.equals("CFS Page")) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    }

    setSelectValue("1");
    body = body.replaceAll(" +/ +", " / ");
    if (super.parseMsg(body, data)) {

      if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
      if (data.strAddress.length() > 0) return true;
    }

    if (alert != null) {
      setFieldList("INFO");
      return data.parseGeneralAlert(this, alert);
    }
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MashField();
    if (name.equals("GRID")) return new MyGridField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("URL")) return new MyInfoURLField();
    return super.getField(name);
  }

  // Parse a mashup of ID, CALL, ADDR, CITY, and Cross streets all of which might
  // or might not be separated by double blank delimiters
  private static final Pattern ID_PTN = Pattern.compile("^(?:([-/* A-Za-z0-9]+)\\b)?(\\d{8})[- ]+");
  private static final String DIST_GRID_PTN_STR = "([A-Z]+) (?:\\(([A-Z][^\\[\\]\\(\\)]+)\\) \\(Pri:(\\d+)\\) \\(Esc:(\\d+)\\) - )?(?:DIST: ([A-Z0-9]*)[- ]+)?GRID: ([A-Z0-9]*) *(.*?)";
  private static final Pattern DIST_GRID_PTN = Pattern.compile("\\["+DIST_GRID_PTN_STR+"\\]");
  private static final Pattern DIST_GRID_PTN2 = Pattern.compile(DIST_GRID_PTN_STR);
  private static final Pattern STANDBY_PTN = Pattern.compile("^STANDBY(?: AT THIS TIME)?  +");
  private static final Pattern STAGE_AT_PTN = Pattern.compile("  +STAGE AT ");
  private static final Pattern JUNK_PTN = Pattern.compile(" (?:\"[^A-Za-z0-9]\"|\"SPECIFY(?: NATURE)?\"|\\{\\{TONE\\}\\}) ");
  private static final Pattern BRACKET_PTN = Pattern.compile(" +(?:\\{([^\\[\\]\\{\\}]*?)\\}|\\[([^\\[\\]\\{\\}]*?)\\]) *");
  private static final Pattern STATE_MBLANK_PTN = Pattern.compile("\\bTX {2,}");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern IN_PTN = Pattern.compile("(.*)(?: IN (?!CUSTODY|CITY|TOWN|WOODS)|, )(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ALPHA_PTN = Pattern.compile("[A-Z]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern TX_PTN = Pattern.compile("TX(?: (?:\\d{5}|0))?\\b *(.*)");
  private class MashField extends Field {

    private boolean newFmt;

    @Override
    public void parse(String field, Data data) {

      // Start with easy stuff.  ID is always the first token
      // Occasionally there is a call prefix in front of it
      Matcher match = ID_PTN.matcher(field);
      if (!match.lookingAt()) abort();
      String prefix = getOptGroup(match.group(1));
      if (prefix.startsWith("CFS")) prefix = "";
      data.strCallId = match.group(2);
      field = field.substring(match.end());

      // Check for one or more map grid constructs
      match = DIST_GRID_PTN.matcher(field);
      if (match.find()) {
        parseDistGrid(match, data);
        String info = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();

        while ((match = DIST_GRID_PTN.matcher(info)).lookingAt()) {
          parseDistGrid(match, data);
          info = info.substring(match.end()).trim();
        }
        data.strSupp = info;
      }

      // New format includes the call description in the grid construct
      newFmt = !data.strCall.isEmpty();

      // If first phrase is a standby request, combine it with second term
      field = STANDBY_PTN.matcher(field).replaceFirst("STANDBY ");

      // Fix leading blanks before STAGE AT phrase
      field = STAGE_AT_PTN.matcher(field).replaceFirst(" STAGE AT");

      // Cleanup other special problems
      field = JUNK_PTN.matcher(field).replaceAll("  ");

      // A field in {} is considered a place name
      match = BRACKET_PTN.matcher(field);
      if (match.find()) {
        String place = match.group(1);
        if (place == null) place = match.group(2);
        data.strPlace = place.trim();
        field = field.substring(0,match.start()) + "  " + field.substring(match.end());
      }

      // Remove extraneous double blank after state
      field = STATE_MBLANK_PTN.matcher(field).replaceFirst("TX ");

      // Break up what is left by any double blank delimiters and see what we have to work with
      String[] flds = MBLANK_PTN.split(field);

      // How we break this down depends on which format we are using
      // New format never has a leading call description, so the first field is always
      // the address.   Second would be a cross street, anything else would be info
      if (newFmt) {
        switch (flds.length) {
          case 1:
            parseAddr(CROSS, flds[0], data);
            break;

          default:
            parseAddr(0, flds[0], data);
            data.strCross = flds[1];
            for (int ndx = 2; ndx<flds.length; ndx++) {
              data.strSupp = append(data.strSupp, "\n", flds[ndx]);
            }
            break;
        }

        // The place name may lead the cross street
        if (data.strPlace.isEmpty()) {
          String cross = data.strCross;
          data.strCross = "";
          parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, cross);
        }
      }

      // Old format is considerably more complicated
      else {
        switch (flds.length) {

        case 1:

          parseAddr(CALL | CROSS, flds[0], data);
          break;

        case 2:

          // Two fields is ambiguous, we don't know if if the break is call/address and cross
          // or call and address/cross.  We'll check both for an IN keyword, which
          // would mark the address
          if (parseAddr(OPTIONAL | CROSS, flds[1], data)) {
            data.strCall = flds[0];
          }

          else {
            parseAddr(CALL, flds[0], data);
            data.strCross = flds[1];
          }
          break;

        default:

          // Three or more fields, special case if next to last field is CROSS STREETS
          int addrPt = -1;

          for (int ii = flds.length-2; addrPt < 0 && ii>=1 && ii >= flds.length-3; ii--) {
            if (flds[ii].equals("CROSS STREETS")) {
              data.strCross = flds[ii+1];
              if (ii+2 < flds.length) data.strSupp = flds[ii+2];
              addrPt = ii-1;
              parseAddr((addrPt == 0 ? CALL : 0), flds[addrPt], data);
            }
          }

          // Otherwise, look for address in one of the last
          // two fields.
          if (addrPt < 0) {
            for (int ii = flds.length-1; addrPt < 0 && ii >= 0; ii--) {
              int flags = OPTIONAL;
              if (ii == 0) flags |= CALL;
              if (ii == flds.length-1) flags |= CROSS;
              if (parseAddr(flags, flds[ii], data)) {
                addrPt = ii;
                if (ii + 1 < flds.length) {
                  data.strCross = flds[ii+1];
                  for (ii = ii+2; ii < flds.length; ii++) {
                    data.strSupp = append(data.strSupp, " - ", flds[ii]);
                  }
                }
              }
            }
          }

          if (addrPt < 0) abort();

          // Any fields in front of address are concatenated to form call description
          for (int ii = 0; ii < addrPt; ii++) {
            data.strCall = append(data.strCall, " - ", flds[ii]);
          }
          break;
        }
      }

      // Append the call prefix if we have one
      if (!data.strCall.startsWith(prefix)) data.strCall = append(prefix, " - ", data.strCall);

      // However we got them, remove leading/trailing / from cross field
      data.strCross = stripFieldStart(data.strCross, "/");
      data.strCross = stripFieldEnd(data.strCross, "/");
    }

    private static final int OPTIONAL = 1;
    private static final int CALL = 2;
    private static final int CROSS = 4;
    private boolean parseAddr(int flags, String sAddress, Data data) {

      // Break out flag options
      boolean optional = (flags & OPTIONAL) != 0;
      boolean call = (flags & CALL) != 0;
      boolean cross = (flags & CROSS) != 0;

      int parseFlags = FLAG_NO_IMPLIED_APT;
      StartType st = StartType.START_ADDR;
      if (call) {
        st = StartType.START_CALL;
//        parseFlags |= FLAG_START_FLD_REQ;
      }

      // Next, see if address contains an IN keyword separated call/address from city/cross
      Matcher match = IN_PTN.matcher(sAddress);
      if (match.find()) {

        sAddress = match.group(1).trim();
        String tail = match.group(2).trim();

        // Check for doubled IN city construct
        match = IN_PTN.matcher(sAddress);
        if (match.matches()) {
          String city = match.group(2).trim();
          boolean good = ALPHA_PTN.matcher(city).matches();
          if (!good) {
            String upCity = city.toUpperCase();
            String city2 = DOUBLE_CITY_LIST.getCode(upCity, true);
            if (city2 != null) {
              good = true;
              if (city2.length() < upCity.length() && (city2.endsWith(" CO") || city.endsWith(" COUNTY"))) {
                city = upCity.substring(city2.length()).trim();
              } else {
                city = city2;
              }
            }
          }
          if (good) {
            sAddress = match.group(1).trim();
            data.strCity = city;
            tail = stripFieldStart(tail, city);
          }
        }

        // Check, use smart parser to split call and address
        parseAddress(st, parseFlags | FLAG_ANCHOR_END, sAddress, data);

        tail = trimTX(tail);

        if (data.strCity.length() > 0) {
          if (cross) setCross(tail, data);
          return true;
        }

        // Otherwise, if we aren't handling cross streets, it is all city
        if (!cross) {
          data.strCity = tail;
          return true;
        }

        // Otherwise, see if it starts with a two word city, if it does
        // use that city to break tail into city and cross streets
        String city = DOUBLE_CITY_LIST.getCode(tail.toUpperCase(), true);
        if (city != null) {
          data.strCity = tail.substring(0,city.length());
          setCross(trimTX(tail.substring(city.length()).trim()), data);
          return true;
        }

        // Otherwise first word of tail is city, rest is cross
        // Unless city was followed by TX which needs to go
        Parser p = new Parser(tail);
        data.strCity = p.get(' ');
        setCross(trimTX(p.get()), data);
        return true;
      }

      // No IN keyword, if this was an optional parse, return failure
      // Unless is an obvious address
      if (optional) {
        Result res = parseAddress(st, parseFlags | FLAG_CHECK_STATUS | FLAG_ANCHOR_END, sAddress);
        if (res.getStatus() < STATUS_FULL_ADDRESS) return false;
        res.getData(data);
        return true;
      }

      // no IN keyword, which we assume means no city
      // Use smart address parser to separate call, address, and cross
      if (!cross) parseFlags |= FLAG_ANCHOR_END;
      parseAddress(st, parseFlags, sAddress, data);
      if (cross) setCross(getLeft(), data);
      return true;
    }

    private String trimTX(String tail) {
      Matcher match;
      match = TX_PTN.matcher(tail);
      if (match.matches()) tail = match.group(1);
      return tail;
    }

    private void setCross(String cross, Data data) {
      if (cross.equals("2ND TONE/PAGE")) {
        data.strCall = append(data.strCall, " - ", cross);
      } else {
        data.strCross = cross;
      }
    }

    @Override
    public String getFieldNames() {
      return newFmt ? "ID ADDR CITY PLACE X SRC CALL PRI MAP INFO"
                    : "ID CALL ADDR CITY PLACE X SRC MAP INFO";
    }
  }

  private class MyGridField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match;
      while ((match = DIST_GRID_PTN.matcher(field)).lookingAt()) {
        parseDistGrid(match, data);
        field = field.substring(match.end()).trim();
      }
      if (!field.isEmpty()) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL? PRI? MAP";
    }

  }

  private void parseDistGrid(Matcher match, Data data) {
    String extra = parseDistGrid2(match, data);
    while (extra.length() > 0) {
      match = DIST_GRID_PTN2.matcher(extra);
      if (!match.matches()) break;
      extra = parseDistGrid2(match, data);
    }
  }

  private String parseDistGrid2(Matcher match, Data data) {
    String src = match.group(1).trim();
    if (!data.strSource.contains(src)) data.strSource = append(data.strSource, " ", src);
    String call = match.group(2);
    if (call != null) {
      if (!data.strCall.contains(call)) data.strCall = append(data.strCall, " - ", call.trim());
      String priority =  append(getOptGroup(match.group(3)), "/", getOptGroup(match.group(4)));
      if (!priority.isEmpty()) data.strPriority = priority;
    }
    String map = append(getOptGroup(match.group(5)), "-", match.group(6).trim());
    if (!data.strMap.equals(map)) data.strMap = append(data.strMap, "/", map);
    return match.group(7).trim();
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("  ");
      if (pt >= 0) {
        String info = field.substring(pt+2).trim();
        if (!info.startsWith("<NO ") && !info.endsWith(" REMARKS>")) {
          data.strSupp = append(data.strSupp, "\n", info);
        }
        field = field.substring(0,pt);
      }
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_MAP_PTN = Pattern.compile("^MAP(?: PAGE)?[ _]?((?:[A-Z]{3} )?[-A-Z0-9]+)(?:\\.PDF)? *");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("(?:\\d\\d:\\d\\d\\b|\\[\\d\\d:\\d\\d:\\d\\d\\])[: ]*");
  private static final Pattern INFO_NONE_PTN = Pattern.compile(" *<NO(?:NE|.* COMMENTS|.* REMARKS)> *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher  match = INFO_MAP_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group(1);
        field = field.substring(match.end());
      }

      match = INFO_TIME_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();

      field = INFO_NONE_PTN.matcher(field).replaceAll(" ").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "MAP TIME INFO";
    }
  }

  private class MyInfoURLField extends InfoUrlField {
    @Override
    public void parse(String field, Data data) {
      super.parse(getRelativeField(0), data);
    }
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(\\d+) *(-\\d+) (.*)");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = ADDR_APT_PTN.matcher(address);
    if (match.matches()) {
      String grp1 = match.group(1);
      String grp2 = match.group(2);
      String grp3 = match.group(3);
      if (grp1.equals("1777")) {
        address = grp1 + ' ' + grp3;
      } else {
        address = grp1 + grp2 + ' ' + grp3;
      }
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "880-101 S COIT RD",  "33.226272,-96.767895",
      "880-102 S COIT RD",  "33.226272,-96.767895",
      "880-103 S COIT RD",  "33.226272,-96.767895",
      "880-104 S COIT RD",  "33.226272,-96.767895",
      "880-105 S COIT RD",  "33.226272,-96.767895",
      "880-106 S COIT RD",  "33.226272,-96.767895",
      "880-107 S COIT RD",  "33.226272,-96.767895",
      "880-108 S COIT RD",  "33.226272,-96.767895",
      "880-109 S COIT RD",  "33.226272,-96.767895",
      "880-110 S COIT RD",  "33.226272,-96.767895",
      "880-111 S COIT RD",  "33.226272,-96.767895",
      "880-112 S COIT RD",  "33.226272,-96.767895",
      "880-201 S COIT RD",  "33.225406,-96.767932",
      "880-202 S COIT RD",  "33.225406,-96.767932",
      "880-203 S COIT RD",  "33.225406,-96.767932",
      "880-204 S COIT RD",  "33.225406,-96.767932",
      "880-205 S COIT RD",  "33.225406,-96.767932",
      "880-206 S COIT RD",  "33.225406,-96.767932",
      "880-207 S COIT RD",  "33.225406,-96.767932",
      "880-301 S COIT RD",  "33.224984,-96.767943",
      "880-302 S COIT RD",  "33.224984,-96.767943",
      "880-303 S COIT RD",  "33.224984,-96.767943",
      "880-304 S COIT RD",  "33.224984,-96.767943",
      "880-305 S COIT RD",  "33.224984,-96.767943",
      "880-306 S COIT RD",  "33.224984,-96.767943",
      "880-307 S COIT RD",  "33.224984,-96.767943",
      "880-401 S COIT RD",  "33.226124,-96.768340",
      "880-402 S COIT RD",  "33.226124,-96.768340",
      "880-403 S COIT RD",  "33.226124,-96.768340",
      "880-404 S COIT RD",  "33.226124,-96.768340",
      "880-405 S COIT RD",  "33.226124,-96.768340",
      "880-406 S COIT RD",  "33.226124,-96.768340",
      "880-407 S COIT RD",  "33.226124,-96.768340",
      "880-501 S COIT RD",  "33.225823,-96.768790",
      "880-502 S COIT RD",  "33.225823,-96.768790",
      "880-503 S COIT RD",  "33.225823,-96.768790",
      "880-504 S COIT RD",  "33.225823,-96.768790",
      "880-505 S COIT RD",  "33.225823,-96.768790",
      "880-506 S COIT RD",  "33.225823,-96.768790",
      "880-507 S COIT RD",  "33.225823,-96.768790",
      "880-601 S COIT RD",  "33.225172,-96.768329",
      "880-602 S COIT RD",  "33.225172,-96.768329",
      "880-603 S COIT RD",  "33.225172,-96.768329",
      "880-604 S COIT RD",  "33.225172,-96.768329",
      "880-605 S COIT RD",  "33.225172,-96.768329",
      "880-606 S COIT RD",  "33.225172,-96.768329",
      "880-607 S COIT RD",  "33.225172,-96.768329",
      "880-701 S COIT RD",  "33.225576,-96.769075",
      "880-702 S COIT RD",  "33.225576,-96.769075",
      "880-703 S COIT RD",  "33.225576,-96.769075",
      "880-704 S COIT RD",  "33.225576,-96.769075",
      "880-705 S COIT RD",  "33.225576,-96.769075",
      "880-706 S COIT RD",  "33.225576,-96.769075",
      "880-707 S COIT RD",  "33.225576,-96.769075",
      "880-801 S COIT RD",  "33.225168,-96.768753",
      "880-802 S COIT RD",  "33.225168,-96.768753",
      "880-803 S COIT RD",  "33.225168,-96.768753",
      "880-804 S COIT RD",  "33.225168,-96.768753",
      "880-805 S COIT RD",  "33.225168,-96.768753",
      "880-806 S COIT RD",  "33.225168,-96.768753",
      "880-807 S COIT RD",  "33.225168,-96.768753",
      "880-901 S COIT RD",  "33.224746,-96.768404",
      "880-902 S COIT RD",  "33.224746,-96.768404",
      "880-903 S COIT RD",  "33.224746,-96.768404",
      "880-904 S COIT RD",  "33.224746,-96.768404",
      "880-905 S COIT RD",  "33.224746,-96.768404",
      "880-906 S COIT RD",  "33.224746,-96.768404",
      "880-907 S COIT RD",  "33.224746,-96.768404",
      "880-1001 S COIT RD",  "33.225365,-96.769418",
      "880-1002 S COIT RD",  "33.225365,-96.769418",
      "880-1003 S COIT RD",  "33.225365,-96.769418",
      "880-1004 S COIT RD",  "33.225365,-96.769418",
      "880-1005 S COIT RD",  "33.225365,-96.769418",
      "880-1006 S COIT RD",  "33.225365,-96.769418",
      "880-1007 S COIT RD",  "33.225365,-96.769418",
      "880-1101 S COIT RD",  "33.225029,-96.769118",
      "880-1102 S COIT RD",  "33.225029,-96.769118",
      "880-1103 S COIT RD",  "33.225029,-96.769118",
      "880-1104 S COIT RD",  "33.225029,-96.769118",
      "880-1105 S COIT RD",  "33.225029,-96.769118",
      "880-1106 S COIT RD",  "33.225029,-96.769118",
      "880-1107 S COIT RD",  "33.225029,-96.769118",
      "880-1201 S COIT RD",  "33.224661,-96.768978",
      "880-1202 S COIT RD",  "33.224661,-96.768978",
      "880-1203 S COIT RD",  "33.224661,-96.768978",
      "880-1204 S COIT RD",  "33.224661,-96.768978",
      "880-1205 S COIT RD",  "33.224661,-96.768978",
      "880-1206 S COIT RD",  "33.224661,-96.768978",
      "880-1207 S COIT RD",  "33.224661,-96.768978",
      "880-1301 S COIT RD",  "33.225163,-96.769815",
      "880-1302 S COIT RD",  "33.225163,-96.769815",
      "880-1303 S COIT RD",  "33.225163,-96.769815",
      "880-1304 S COIT RD",  "33.225163,-96.769815",
      "880-1305 S COIT RD",  "33.225163,-96.769815",
      "880-1306 S COIT RD",  "33.225163,-96.769815",
      "880-1307 S COIT RD",  "33.225163,-96.769815",
      "880-1308 S COIT RD",  "33.225163,-96.769815",
      "880-1309 S COIT RD",  "33.225163,-96.769815",
      "880-1310 S COIT RD",  "33.225163,-96.769815",
      "880-1311 S COIT RD",  "33.225163,-96.769815",
      "880-1312 S COIT RD",  "33.225163,-96.769815",
      "880-1401 S COIT RD",  "33.224701,-96.769525",
      "880-1402 S COIT RD",  "33.224701,-96.769525",
      "880-1403 S COIT RD",  "33.224701,-96.769525",
      "880-1404 S COIT RD",  "33.224701,-96.769525",
      "880-1405 S COIT RD",  "33.224701,-96.769525",
      "880-1406 S COIT RD",  "33.224701,-96.769525",
      "880-1407 S COIT RD",  "33.224701,-96.769525",
      "880-1408 S COIT RD",  "33.224701,-96.769525",
      "880-1409 S COIT RD",  "33.224701,-96.769525",
      "880-1410 S COIT RD",  "33.224701,-96.769525",
      "880-1411 S COIT RD",  "33.224701,-96.769525",
      "880-1412 S COIT RD",  "33.224701,-96.769525",
      "880-1501 S COIT RD",  "33.224392,-96.769402",
      "880-1502 S COIT RD",  "33.224392,-96.769402",
      "880-1503 S COIT RD",  "33.224392,-96.769402",
      "880-1504 S COIT RD",  "33.224392,-96.769402",
      "880-1505 S COIT RD",  "33.224392,-96.769402",
      "880-1506 S COIT RD",  "33.224392,-96.769402",
      "880-1507 S COIT RD",  "33.224392,-96.769402",
      "880-1601 S COIT RD",  "33.224593,-96.770641",
      "880-1602 S COIT RD",  "33.224593,-96.770641",
      "880-1603 S COIT RD",  "33.224593,-96.770641",
      "880-1604 S COIT RD",  "33.224593,-96.770641",
      "880-1605 S COIT RD",  "33.224593,-96.770641",
      "880-1606 S COIT RD",  "33.224593,-96.770641",
      "880-1607 S COIT RD",  "33.224593,-96.770641",
      "880-1701 S COIT RD",  "33.224553,-96.77004",
      "880-1702 S COIT RD",  "33.224553,-96.77004",
      "880-1703 S COIT RD",  "33.224553,-96.77004",
      "880-1704 S COIT RD",  "33.224553,-96.77004",
      "880-1705 S COIT RD",  "33.224553,-96.77004",
      "880-1706 S COIT RD",  "33.224553,-96.77004",
      "880-1707 S COIT RD",  "33.224553,-96.77004",
      "880-1801 S COIT RD",  "33.224127,-96.769949",
      "880-1802 S COIT RD",  "33.224127,-96.769949",
      "880-1803 S COIT RD",  "33.224127,-96.769949",
      "880-1804 S COIT RD",  "33.224127,-96.769949",
      "880-1805 S COIT RD",  "33.224127,-96.769949",
      "880-1806 S COIT RD",  "33.224127,-96.769949",
      "880-1807 S COIT RD",  "33.224127,-96.769949",
      "880-1901 S COIT RD",  "33.224127,-96.769949",
      "880-1902 S COIT RD",  "33.224351,-96.770362",
      "880-1903 S COIT RD",  "33.224351,-96.770362",
      "880-1904 S COIT RD",  "33.224351,-96.770362",
      "880-1905 S COIT RD",  "33.224351,-96.770362",
      "880-1906 S COIT RD",  "33.224351,-96.770362",
      "880-1907 S COIT RD",  "33.224351,-96.770362",
      "880-2001 S COIT RD",  "33.224849,-96.77025",
      "880-2002 S COIT RD",  "33.224849,-96.77025",
      "880-2003 S COIT RD",  "33.224849,-96.77025",
      "880-2004 S COIT RD",  "33.224849,-96.77025",
      "880-2005 S COIT RD",  "33.224849,-96.77025",
      "880-2006 S COIT RD",  "33.224849,-96.77025",
      "880-2007 S COIT RD",  "33.224849,-96.77025",
      "880-2101 S COIT RD",  "33.224315,-96.771151",
      "880-2102 S COIT RD",  "33.224315,-96.771151",
      "880-2103 S COIT RD",  "33.224315,-96.771151",
      "880-2104 S COIT RD",  "33.224315,-96.771151",
      "880-2105 S COIT RD",  "33.224315,-96.771151",
      "880-2106 S COIT RD",  "33.224315,-96.771151",
      "880-2107 S COIT RD",  "33.224315,-96.771151",
      "880-2108 S COIT RD",  "33.224315,-96.771151",
      "880-2109 S COIT RD",  "33.224315,-96.771151",
      "880-2110 S COIT RD",  "33.224315,-96.771151",
      "880-2111 S COIT RD",  "33.224315,-96.771151",
      "880-2112 S COIT RD",  "33.224315,-96.771151",
      "880-2201 S COIT RD",  "33.223840,-96.771746",
      "880-2202 S COIT RD",  "33.223840,-96.771746",
      "880-2203 S COIT RD",  "33.223840,-96.771746",
      "880-2204 S COIT RD",  "33.223840,-96.771746",
      "880-2205 S COIT RD",  "33.223840,-96.771746",
      "880-2206 S COIT RD",  "33.223840,-96.771746",
      "880-2207 S COIT RD",  "33.223840,-96.771746",
      "880-2301 S COIT RD",  "33.223611,-96.771564",
      "880-2302 S COIT RD",  "33.223611,-96.771564",
      "880-2303 S COIT RD",  "33.223611,-96.771564",
      "880-2304 S COIT RD",  "33.223611,-96.771564",
      "880-2305 S COIT RD",  "33.223611,-96.771564",
      "880-2306 S COIT RD",  "33.223611,-96.771564",
      "880-2307 S COIT RD",  "33.223611,-96.771564",
      "880-2401 S COIT RD",  "33.223373,-96.771317",
      "880-2402 S COIT RD",  "33.223373,-96.771317",
      "880-2403 S COIT RD",  "33.223373,-96.771317",
      "880-2404 S COIT RD",  "33.223373,-96.771317",
      "880-2405 S COIT RD",  "33.223373,-96.771317",
      "880-2406 S COIT RD",  "33.223373,-96.771317",
      "880-2407 S COIT RD",  "33.223373,-96.771317",
      "880-2501 S COIT RD",  "33.223548,-96.772186",
      "880-2502 S COIT RD",  "33.223548,-96.772186",
      "880-2503 S COIT RD",  "33.223548,-96.772186",
      "880-2504 S COIT RD",  "33.223548,-96.772186",
      "880-2505 S COIT RD",  "33.223548,-96.772186",
      "880-2506 S COIT RD",  "33.223548,-96.772186",
      "880-2507 S COIT RD",  "33.223548,-96.772186",
      "880-2601 S COIT RD",  "33.223297,-96.771982",
      "880-2602 S COIT RD",  "33.223297,-96.771982",
      "880-2603 S COIT RD",  "33.223297,-96.771982",
      "880-2604 S COIT RD",  "33.223297,-96.771982",
      "880-2605 S COIT RD",  "33.223297,-96.771982",
      "880-2606 S COIT RD",  "33.223297,-96.771982",
      "880-2607 S COIT RD",  "33.223297,-96.771982",
      "880-2701 S COIT RD",  "33.223059,-96.771709",
      "880-2702 S COIT RD",  "33.223059,-96.771709",
      "880-2703 S COIT RD",  "33.223059,-96.771709",
      "880-2704 S COIT RD",  "33.223059,-96.771709",
      "880-2705 S COIT RD",  "33.223059,-96.771709",
      "880-2706 S COIT RD",  "33.223059,-96.771709",
      "880-2707 S COIT RD",  "33.223059,-96.771709",
      "880-2801 S COIT RD",  "33.223323,-96.772626",
      "880-2802 S COIT RD",  "33.223323,-96.772626",
      "880-2803 S COIT RD",  "33.223323,-96.772626",
      "880-2804 S COIT RD",  "33.223323,-96.772626",
      "880-2805 S COIT RD",  "33.223323,-96.772626",
      "880-2806 S COIT RD",  "33.223323,-96.772626",
      "880-2807 S COIT RD",  "33.223323,-96.772626",
      "880-2808 S COIT RD",  "33.223323,-96.772626",
      "880-2809 S COIT RD",  "33.223323,-96.772626",
      "880-2810 S COIT RD",  "33.223323,-96.772626",
      "880-2811 S COIT RD",  "33.223323,-96.772626",
      "880-2812 S COIT RD",  "33.223323,-96.772626",
      "880-2901 S COIT RD",  "33.223045,-96.772363",
      "880-2902 S COIT RD",  "33.223045,-96.772363",
      "880-2903 S COIT RD",  "33.223045,-96.772363",
      "880-2904 S COIT RD",  "33.223045,-96.772363",
      "880-2905 S COIT RD",  "33.223045,-96.772363",
      "880-2906 S COIT RD",  "33.223045,-96.772363",
      "880-2907 S COIT RD",  "33.223045,-96.772363",
      "880-3001 S COIT RD",  "33.222794,-96.772127",
      "880-3002 S COIT RD",  "33.222794,-96.772127",
      "880-3003 S COIT RD",  "33.222794,-96.772127",
      "880-3004 S COIT RD",  "33.222794,-96.772127",
      "880-3005 S COIT RD",  "33.222794,-96.772127",
      "880-3006 S COIT RD",  "33.222794,-96.772127",
      "880-3007 S COIT RD",  "33.222794,-96.772127",
      "880-3101 S COIT RD",  "33.222987,-96.773061",
      "880-3102 S COIT RD",  "33.222987,-96.773061",
      "880-3103 S COIT RD",  "33.222987,-96.773061",
      "880-3104 S COIT RD",  "33.222987,-96.773061",
      "880-3105 S COIT RD",  "33.222987,-96.773061",
      "880-3106 S COIT RD",  "33.222987,-96.773061",
      "880-3107 S COIT RD",  "33.222987,-96.773061",
      "880-3201 S COIT RD",  "33.222758,-96.772825",
      "880-3202 S COIT RD",  "33.222758,-96.772825",
      "880-3203 S COIT RD",  "33.222758,-96.772825",
      "880-3204 S COIT RD",  "33.222758,-96.772825",
      "880-3205 S COIT RD",  "33.222758,-96.772825",
      "880-3206 S COIT RD",  "33.222758,-96.772825",
      "880-3207 S COIT RD",  "33.222758,-96.772825",
      "880-3301 S COIT RD",  "33.222502,-96.772567",
      "880-3302 S COIT RD",  "33.222502,-96.772567",
      "880-3303 S COIT RD",  "33.222502,-96.772567",
      "880-3304 S COIT RD",  "33.222502,-96.772567",
      "880-3305 S COIT RD",  "33.222502,-96.772567",
      "880-3306 S COIT RD",  "33.222502,-96.772567",
      "880-3307 S COIT RD",  "33.222502,-96.772567",
      "880-3401 S COIT RD",  "33.222677,-96.773479",
      "880-3402 S COIT RD",  "33.222677,-96.773479",
      "880-3403 S COIT RD",  "33.222677,-96.773479",
      "880-3404 S COIT RD",  "33.222677,-96.773479",
      "880-3405 S COIT RD",  "33.222677,-96.773479",
      "880-3406 S COIT RD",  "33.222677,-96.773479",
      "880-3407 S COIT RD",  "33.222677,-96.773479",
      "880-3501 S COIT RD",  "33.222395,-96.773785",
      "880-3502 S COIT RD",  "33.222395,-96.773785",
      "880-3503 S COIT RD",  "33.222395,-96.773785",
      "880-3504 S COIT RD",  "33.222395,-96.773785",
      "880-3505 S COIT RD",  "33.222395,-96.773785",
      "880-3506 S COIT RD",  "33.222395,-96.773785",
      "880-3507 S COIT RD",  "33.222395,-96.773785",
      "880-3508 S COIT RD",  "33.222395,-96.773785",
      "880-3509 S COIT RD",  "33.222395,-96.773785",
      "880-3510 S COIT RD",  "33.222395,-96.773785",
      "880-3511 S COIT RD",  "33.222395,-96.773785",
      "880-3512 S COIT RD",  "33.222395,-96.773785",
      "880-3601 S COIT RD",  "33.222435,-96.77327",
      "880-3602 S COIT RD",  "33.222435,-96.77327",
      "880-3603 S COIT RD",  "33.222435,-96.77327",
      "880-3604 S COIT RD",  "33.222435,-96.77327",
      "880-3605 S COIT RD",  "33.222435,-96.77327",
      "880-3606 S COIT RD",  "33.222435,-96.77327",
      "880-3607 S COIT RD",  "33.222435,-96.77327",
      "880-3701 S COIT RD",  "33.222076,-96.773495",
      "880-3702 S COIT RD",  "33.222076,-96.773495",
      "880-3703 S COIT RD",  "33.222076,-96.773495",
      "880-3704 S COIT RD",  "33.222076,-96.773495",
      "880-3705 S COIT RD",  "33.222076,-96.773495",
      "880-3706 S COIT RD",  "33.222076,-96.773495",
      "880-3707 S COIT RD",  "33.222076,-96.773495",
      "880-3708 S COIT RD",  "33.222076,-96.773495",
      "880-3709 S COIT RD",  "33.222076,-96.773495",
      "880-3710 S COIT RD",  "33.222076,-96.773495",
      "880-3711 S COIT RD",  "33.222076,-96.773495",
      "880-3712 S COIT RD",  "33.222076,-96.773495",
      "880-3801 S COIT RD",  "33.222076,-96.774101",
      "880-3802 S COIT RD",  "33.222076,-96.774101",
      "880-3803 S COIT RD",  "33.222076,-96.774101",
      "880-3804 S COIT RD",  "33.222076,-96.774101",
      "880-3805 S COIT RD",  "33.222076,-96.774101",
      "880-3806 S COIT RD",  "33.222076,-96.774101",
      "880-3807 S COIT RD",  "33.222076,-96.774101",
      "880-3901 S COIT RD",  "33.222206,-96.772921",
      "880-3902 S COIT RD",  "33.222206,-96.772921",
      "880-3903 S COIT RD",  "33.222206,-96.772921",
      "880-3904 S COIT RD",  "33.222206,-96.772921",
      "880-3905 S COIT RD",  "33.222206,-96.772921",
      "880-3906 S COIT RD",  "33.222206,-96.772921",
      "880-3907 S COIT RD",  "33.222206,-96.772921",

      "980-110 S COIT RD",  "33.223826,-96.768216",
      "980-111 S COIT RD",  "33.223826,-96.768216",
      "980-112 S COIT RD",  "33.223826,-96.768216",
      "980-113 S COIT RD",  "33.223826,-96.768216",
      "980-114 S COIT RD",  "33.223826,-96.768216",
      "980-115 S COIT RD",  "33.223826,-96.768216",
      "980-116 S COIT RD",  "33.223826,-96.768216",
      "980-117 S COIT RD",  "33.223826,-96.768216",
      "980-120 S COIT RD",  "33.223826,-96.768216",
      "980-121 S COIT RD",  "33.223826,-96.768216",
      "980-122 S COIT RD",  "33.223826,-96.768216",
      "980-123 S COIT RD",  "33.223826,-96.768216",
      "980-124 S COIT RD",  "33.223826,-96.768216",
      "980-125 S COIT RD",  "33.223826,-96.768216",
      "980-126 S COIT RD",  "33.223826,-96.768216",
      "980-127 S COIT RD",  "33.223826,-96.768216",
      "980-130 S COIT RD",  "33.223826,-96.768216",
      "980-131 S COIT RD",  "33.223826,-96.768216",
      "980-132 S COIT RD",  "33.223826,-96.768216",
      "980-133 S COIT RD",  "33.223826,-96.768216",
      "980-134 S COIT RD",  "33.223826,-96.768216",
      "980-135 S COIT RD",  "33.223826,-96.768216",
      "980-136 S COIT RD",  "33.223826,-96.768216",
      "980-137 S COIT RD",  "33.223826,-96.768216",
      "980-210 S COIT RD",  "33.223507,-96.768533",
      "980-211 S COIT RD",  "33.223507,-96.768533",
      "980-212 S COIT RD",  "33.223507,-96.768533",
      "980-213 S COIT RD",  "33.223507,-96.768533",
      "980-214 S COIT RD",  "33.223507,-96.768533",
      "980-215 S COIT RD",  "33.223507,-96.768533",
      "980-216 S COIT RD",  "33.223507,-96.768533",
      "980-217 S COIT RD",  "33.223507,-96.768533",
      "980-220 S COIT RD",  "33.223507,-96.768533",
      "980-221 S COIT RD",  "33.223507,-96.768533",
      "980-222 S COIT RD",  "33.223507,-96.768533",
      "980-223 S COIT RD",  "33.223507,-96.768533",
      "980-224 S COIT RD",  "33.223507,-96.768533",
      "980-225 S COIT RD",  "33.223507,-96.768533",
      "980-226 S COIT RD",  "33.223507,-96.768533",
      "980-227 S COIT RD",  "33.223507,-96.768533",
      "980-230 S COIT RD",  "33.223507,-96.768533",
      "980-231 S COIT RD",  "33.223507,-96.768533",
      "980-232 S COIT RD",  "33.223507,-96.768533",
      "980-233 S COIT RD",  "33.223507,-96.768533",
      "980-234 S COIT RD",  "33.223507,-96.768533",
      "980-235 S COIT RD",  "33.223507,-96.768533",
      "980-236 S COIT RD",  "33.223507,-96.768533",
      "980-237 S COIT RD",  "33.223507,-96.768533",
      "980-310 S COIT RD",  "33.223409,-96.769037",
      "980-311 S COIT RD",  "33.223409,-96.769037",
      "980-312 S COIT RD",  "33.223409,-96.769037",
      "980-313 S COIT RD",  "33.223409,-96.769037",
      "980-314 S COIT RD",  "33.223409,-96.769037",
      "980-315 S COIT RD",  "33.223409,-96.769037",
      "980-316 S COIT RD",  "33.223409,-96.769037",
      "980-317 S COIT RD",  "33.223409,-96.769037",
      "980-320 S COIT RD",  "33.223409,-96.769037",
      "980-321 S COIT RD",  "33.223409,-96.769037",
      "980-322 S COIT RD",  "33.223409,-96.769037",
      "980-323 S COIT RD",  "33.223409,-96.769037",
      "980-324 S COIT RD",  "33.223409,-96.769037",
      "980-325 S COIT RD",  "33.223409,-96.769037",
      "980-326 S COIT RD",  "33.223409,-96.769037",
      "980-327 S COIT RD",  "33.223409,-96.769037",
      "980-330 S COIT RD",  "33.223409,-96.769037",
      "980-331 S COIT RD",  "33.223409,-96.769037",
      "980-332 S COIT RD",  "33.223409,-96.769037",
      "980-333 S COIT RD",  "33.223409,-96.769037",
      "980-334 S COIT RD",  "33.223409,-96.769037",
      "980-335 S COIT RD",  "33.223409,-96.769037",
      "980-336 S COIT RD",  "33.223409,-96.769037",
      "980-337 S COIT RD",  "33.223409,-96.769037",
      "980-410 S COIT RD",  "33.223826,-96.769445",
      "980-411 S COIT RD",  "33.223826,-96.769445",
      "980-412 S COIT RD",  "33.223826,-96.769445",
      "980-413 S COIT RD",  "33.223826,-96.769445",
      "980-414 S COIT RD",  "33.223826,-96.769445",
      "980-415 S COIT RD",  "33.223826,-96.769445",
      "980-416 S COIT RD",  "33.223826,-96.769445",
      "980-417 S COIT RD",  "33.223826,-96.769445",
      "980-420 S COIT RD",  "33.223826,-96.769445",
      "980-421 S COIT RD",  "33.223826,-96.769445",
      "980-422 S COIT RD",  "33.223826,-96.769445",
      "980-423 S COIT RD",  "33.223826,-96.769445",
      "980-424 S COIT RD",  "33.223826,-96.769445",
      "980-425 S COIT RD",  "33.223826,-96.769445",
      "980-426 S COIT RD",  "33.223826,-96.769445",
      "980-427 S COIT RD",  "33.223826,-96.769445",
      "980-430 S COIT RD",  "33.223826,-96.769445",
      "980-431 S COIT RD",  "33.223826,-96.769445",
      "980-432 S COIT RD",  "33.223826,-96.769445",
      "980-433 S COIT RD",  "33.223826,-96.769445",
      "980-434 S COIT RD",  "33.223826,-96.769445",
      "980-435 S COIT RD",  "33.223826,-96.769445",
      "980-436 S COIT RD",  "33.223826,-96.769445",
      "980-437 S COIT RD",  "33.223826,-96.769445",
      "980-510 S COIT RD",  "33.223503,-96.770191",
      "980-511 S COIT RD",  "33.223503,-96.770191",
      "980-512 S COIT RD",  "33.223503,-96.770191",
      "980-513 S COIT RD",  "33.223503,-96.770191",
      "980-514 S COIT RD",  "33.223503,-96.770191",
      "980-515 S COIT RD",  "33.223503,-96.770191",
      "980-516 S COIT RD",  "33.223503,-96.770191",
      "980-517 S COIT RD",  "33.223503,-96.770191",
      "980-520 S COIT RD",  "33.223503,-96.770191",
      "980-521 S COIT RD",  "33.223503,-96.770191",
      "980-522 S COIT RD",  "33.223503,-96.770191",
      "980-523 S COIT RD",  "33.223503,-96.770191",
      "980-524 S COIT RD",  "33.223503,-96.770191",
      "980-525 S COIT RD",  "33.223503,-96.770191",
      "980-526 S COIT RD",  "33.223503,-96.770191",
      "980-527 S COIT RD",  "33.223503,-96.770191",
      "980-530 S COIT RD",  "33.223503,-96.770191",
      "980-531 S COIT RD",  "33.223503,-96.770191",
      "980-532 S COIT RD",  "33.223503,-96.770191",
      "980-533 S COIT RD",  "33.223503,-96.770191",
      "980-534 S COIT RD",  "33.223503,-96.770191",
      "980-535 S COIT RD",  "33.223503,-96.770191",
      "980-536 S COIT RD",  "33.223503,-96.770191",
      "980-537 S COIT RD",  "33.223503,-96.770191",
      "980-610 S COIT RD",  "33.223072,-96.76981",
      "980-611 S COIT RD",  "33.223072,-96.76981",
      "980-612 S COIT RD",  "33.223072,-96.76981",
      "980-613 S COIT RD",  "33.223072,-96.76981",
      "980-614 S COIT RD",  "33.223072,-96.76981",
      "980-615 S COIT RD",  "33.223072,-96.76981",
      "980-616 S COIT RD",  "33.223072,-96.76981",
      "980-617 S COIT RD",  "33.223072,-96.76981",
      "980-620 S COIT RD",  "33.223072,-96.76981",
      "980-621 S COIT RD",  "33.223072,-96.76981",
      "980-622 S COIT RD",  "33.223072,-96.76981",
      "980-623 S COIT RD",  "33.223072,-96.76981",
      "980-624 S COIT RD",  "33.223072,-96.76981",
      "980-625 S COIT RD",  "33.223072,-96.76981",
      "980-626 S COIT RD",  "33.223072,-96.76981",
      "980-627 S COIT RD",  "33.223072,-96.76981",
      "980-630 S COIT RD",  "33.223072,-96.76981",
      "980-631 S COIT RD",  "33.223072,-96.76981",
      "980-632 S COIT RD",  "33.223072,-96.76981",
      "980-633 S COIT RD",  "33.223072,-96.76981",
      "980-634 S COIT RD",  "33.223072,-96.76981",
      "980-635 S COIT RD",  "33.223072,-96.76981",
      "980-636 S COIT RD",  "33.223072,-96.76981",
      "980-637 S COIT RD",  "33.223072,-96.76981",
      "980-710 S COIT RD",  "33.222825,-96.769584",
      "980-711 S COIT RD",  "33.222825,-96.769584",
      "980-712 S COIT RD",  "33.222825,-96.769584",
      "980-713 S COIT RD",  "33.222825,-96.769584",
      "980-714 S COIT RD",  "33.222825,-96.769584",
      "980-715 S COIT RD",  "33.222825,-96.769584",
      "980-716 S COIT RD",  "33.222825,-96.769584",
      "980-717 S COIT RD",  "33.222825,-96.769584",
      "980-720 S COIT RD",  "33.222825,-96.769584",
      "980-721 S COIT RD",  "33.222825,-96.769584",
      "980-722 S COIT RD",  "33.222825,-96.769584",
      "980-723 S COIT RD",  "33.222825,-96.769584",
      "980-724 S COIT RD",  "33.222825,-96.769584",
      "980-725 S COIT RD",  "33.222825,-96.769584",
      "980-726 S COIT RD",  "33.222825,-96.769584",
      "980-727 S COIT RD",  "33.222825,-96.769584",
      "980-730 S COIT RD",  "33.222825,-96.769584",
      "980-731 S COIT RD",  "33.222825,-96.769584",
      "980-732 S COIT RD",  "33.222825,-96.769584",
      "980-733 S COIT RD",  "33.222825,-96.769584",
      "980-734 S COIT RD",  "33.222825,-96.769584",
      "980-735 S COIT RD",  "33.222825,-96.769584",
      "980-736 S COIT RD",  "33.222825,-96.769584",
      "980-737 S COIT RD",  "33.222825,-96.769584",
      "980-810 S COIT RD",  "33.223104,-96.770695",
      "980-811 S COIT RD",  "33.223104,-96.770695",
      "980-812 S COIT RD",  "33.223104,-96.770695",
      "980-813 S COIT RD",  "33.223104,-96.770695",
      "980-814 S COIT RD",  "33.223104,-96.770695",
      "980-815 S COIT RD",  "33.223104,-96.770695",
      "980-816 S COIT RD",  "33.223104,-96.770695",
      "980-817 S COIT RD",  "33.223104,-96.770695",
      "980-820 S COIT RD",  "33.223104,-96.770695",
      "980-821 S COIT RD",  "33.223104,-96.770695",
      "980-822 S COIT RD",  "33.223104,-96.770695",
      "980-823 S COIT RD",  "33.223104,-96.770695",
      "980-824 S COIT RD",  "33.223104,-96.770695",
      "980-825 S COIT RD",  "33.223104,-96.770695",
      "980-826 S COIT RD",  "33.223104,-96.770695",
      "980-827 S COIT RD",  "33.223104,-96.770695",
      "980-830 S COIT RD",  "33.223104,-96.770695",
      "980-831 S COIT RD",  "33.223104,-96.770695",
      "980-832 S COIT RD",  "33.223104,-96.770695",
      "980-833 S COIT RD",  "33.223104,-96.770695",
      "980-834 S COIT RD",  "33.223104,-96.770695",
      "980-835 S COIT RD",  "33.223104,-96.770695",
      "980-836 S COIT RD",  "33.223104,-96.770695",
      "980-837 S COIT RD",  "33.223104,-96.770695",
      "980-910 S COIT RD",  "33.222727,-96.770335",
      "980-911 S COIT RD",  "33.222727,-96.770335",
      "980-912 S COIT RD",  "33.222727,-96.770335",
      "980-913 S COIT RD",  "33.222727,-96.770335",
      "980-914 S COIT RD",  "33.222727,-96.770335",
      "980-915 S COIT RD",  "33.222727,-96.770335",
      "980-916 S COIT RD",  "33.222727,-96.770335",
      "980-917 S COIT RD",  "33.222727,-96.770335",
      "980-920 S COIT RD",  "33.222727,-96.770335",
      "980-921 S COIT RD",  "33.222727,-96.770335",
      "980-922 S COIT RD",  "33.222727,-96.770335",
      "980-923 S COIT RD",  "33.222727,-96.770335",
      "980-924 S COIT RD",  "33.222727,-96.770335",
      "980-925 S COIT RD",  "33.222727,-96.770335",
      "980-926 S COIT RD",  "33.222727,-96.770335",
      "980-927 S COIT RD",  "33.222727,-96.770335",
      "980-930 S COIT RD",  "33.222727,-96.770335",
      "980-931 S COIT RD",  "33.222727,-96.770335",
      "980-932 S COIT RD",  "33.222727,-96.770335",
      "980-933 S COIT RD",  "33.222727,-96.770335",
      "980-934 S COIT RD",  "33.222727,-96.770335",
      "980-935 S COIT RD",  "33.222727,-96.770335",
      "980-936 S COIT RD",  "33.222727,-96.770335",
      "980-937 S COIT RD",  "33.222727,-96.770335",
      "980-1010 S COIT RD",  "33.222444,-96.77011",
      "980-1011 S COIT RD",  "33.222444,-96.77011",
      "980-1012 S COIT RD",  "33.222444,-96.77011",
      "980-1013 S COIT RD",  "33.222444,-96.77011",
      "980-1014 S COIT RD",  "33.222444,-96.77011",
      "980-1015 S COIT RD",  "33.222444,-96.77011",
      "980-1016 S COIT RD",  "33.222444,-96.77011",
      "980-1017 S COIT RD",  "33.222444,-96.77011",
      "980-1020 S COIT RD",  "33.222444,-96.77011",
      "980-1021 S COIT RD",  "33.222444,-96.77011",
      "980-1022 S COIT RD",  "33.222444,-96.77011",
      "980-1023 S COIT RD",  "33.222444,-96.77011",
      "980-1024 S COIT RD",  "33.222444,-96.77011",
      "980-1025 S COIT RD",  "33.222444,-96.77011",
      "980-1026 S COIT RD",  "33.222444,-96.77011",
      "980-1027 S COIT RD",  "33.222444,-96.77011",
      "980-1030 S COIT RD",  "33.222444,-96.77011",
      "980-1031 S COIT RD",  "33.222444,-96.77011",
      "980-1032 S COIT RD",  "33.222444,-96.77011",
      "980-1033 S COIT RD",  "33.222444,-96.77011",
      "980-1034 S COIT RD",  "33.222444,-96.77011",
      "980-1035 S COIT RD",  "33.222444,-96.77011",
      "980-1036 S COIT RD",  "33.222444,-96.77011",
      "980-1037 S COIT RD",  "33.222444,-96.77011",
      "980-1110 S COIT RD",  "33.222852,-96.771172",
      "980-1111 S COIT RD",  "33.222852,-96.771172",
      "980-1112 S COIT RD",  "33.222852,-96.771172",
      "980-1113 S COIT RD",  "33.222852,-96.771172",
      "980-1114 S COIT RD",  "33.222852,-96.771172",
      "980-1115 S COIT RD",  "33.222852,-96.771172",
      "980-1116 S COIT RD",  "33.222852,-96.771172",
      "980-1117 S COIT RD",  "33.222852,-96.771172",
      "980-1120 S COIT RD",  "33.222852,-96.771172",
      "980-1121 S COIT RD",  "33.222852,-96.771172",
      "980-1122 S COIT RD",  "33.222852,-96.771172",
      "980-1123 S COIT RD",  "33.222852,-96.771172",
      "980-1124 S COIT RD",  "33.222852,-96.771172",
      "980-1125 S COIT RD",  "33.222852,-96.771172",
      "980-1126 S COIT RD",  "33.222852,-96.771172",
      "980-1127 S COIT RD",  "33.222852,-96.771172",
      "980-1130 S COIT RD",  "33.222852,-96.771172",
      "980-1131 S COIT RD",  "33.222852,-96.771172",
      "980-1132 S COIT RD",  "33.222852,-96.771172",
      "980-1133 S COIT RD",  "33.222852,-96.771172",
      "980-1134 S COIT RD",  "33.222852,-96.771172",
      "980-1135 S COIT RD",  "33.222852,-96.771172",
      "980-1136 S COIT RD",  "33.222852,-96.771172",
      "980-1137 S COIT RD",  "33.222852,-96.771172",
      "980-1210 S COIT RD",  "33.222354,-96.770652",
      "980-1211 S COIT RD",  "33.222354,-96.770652",
      "980-1212 S COIT RD",  "33.222354,-96.770652",
      "980-1213 S COIT RD",  "33.222354,-96.770652",
      "980-1214 S COIT RD",  "33.222354,-96.770652",
      "980-1215 S COIT RD",  "33.222354,-96.770652",
      "980-1216 S COIT RD",  "33.222354,-96.770652",
      "980-1217 S COIT RD",  "33.222354,-96.770652",
      "980-1220 S COIT RD",  "33.222354,-96.770652",
      "980-1221 S COIT RD",  "33.222354,-96.770652",
      "980-1222 S COIT RD",  "33.222354,-96.770652",
      "980-1223 S COIT RD",  "33.222354,-96.770652",
      "980-1224 S COIT RD",  "33.222354,-96.770652",
      "980-1225 S COIT RD",  "33.222354,-96.770652",
      "980-1226 S COIT RD",  "33.222354,-96.770652",
      "980-1227 S COIT RD",  "33.222354,-96.770652",
      "980-1230 S COIT RD",  "33.222354,-96.770652",
      "980-1231 S COIT RD",  "33.222354,-96.770652",
      "980-1232 S COIT RD",  "33.222354,-96.770652",
      "980-1233 S COIT RD",  "33.222354,-96.770652",
      "980-1234 S COIT RD",  "33.222354,-96.770652",
      "980-1235 S COIT RD",  "33.222354,-96.770652",
      "980-1236 S COIT RD",  "33.222354,-96.770652",
      "980-1237 S COIT RD",  "33.222354,-96.770652",
      "980-1310 S COIT RD",  "33.222592,-96.771559",
      "980-1311 S COIT RD",  "33.222592,-96.771559",
      "980-1312 S COIT RD",  "33.222592,-96.771559",
      "980-1313 S COIT RD",  "33.222592,-96.771559",
      "980-1314 S COIT RD",  "33.222592,-96.771559",
      "980-1315 S COIT RD",  "33.222592,-96.771559",
      "980-1316 S COIT RD",  "33.222592,-96.771559",
      "980-1317 S COIT RD",  "33.222592,-96.771559",
      "980-1320 S COIT RD",  "33.222592,-96.771559",
      "980-1321 S COIT RD",  "33.222592,-96.771559",
      "980-1322 S COIT RD",  "33.222592,-96.771559",
      "980-1323 S COIT RD",  "33.222592,-96.771559",
      "980-1324 S COIT RD",  "33.222592,-96.771559",
      "980-1325 S COIT RD",  "33.222592,-96.771559",
      "980-1326 S COIT RD",  "33.222592,-96.771559",
      "980-1327 S COIT RD",  "33.222592,-96.771559",
      "980-1330 S COIT RD",  "33.222592,-96.771559",
      "980-1331 S COIT RD",  "33.222592,-96.771559",
      "980-1332 S COIT RD",  "33.222592,-96.771559",
      "980-1333 S COIT RD",  "33.222592,-96.771559",
      "980-1334 S COIT RD",  "33.222592,-96.771559",
      "980-1335 S COIT RD",  "33.222592,-96.771559",
      "980-1336 S COIT RD",  "33.222592,-96.771559",
      "980-1337 S COIT RD",  "33.222592,-96.771559",
      "980-1410 S COIT RD",  "33.22217,-96.771859",
      "980-1411 S COIT RD",  "33.22217,-96.771859",
      "980-1412 S COIT RD",  "33.22217,-96.771859",
      "980-1413 S COIT RD",  "33.22217,-96.771859",
      "980-1414 S COIT RD",  "33.22217,-96.771859",
      "980-1415 S COIT RD",  "33.22217,-96.771859",
      "980-1416 S COIT RD",  "33.22217,-96.771859",
      "980-1417 S COIT RD",  "33.22217,-96.771859",
      "980-1420 S COIT RD",  "33.22217,-96.771859",
      "980-1421 S COIT RD",  "33.22217,-96.771859",
      "980-1422 S COIT RD",  "33.22217,-96.771859",
      "980-1423 S COIT RD",  "33.22217,-96.771859",
      "980-1424 S COIT RD",  "33.22217,-96.771859",
      "980-1425 S COIT RD",  "33.22217,-96.771859",
      "980-1426 S COIT RD",  "33.22217,-96.771859",
      "980-1427 S COIT RD",  "33.22217,-96.771859",
      "980-1430 S COIT RD",  "33.22217,-96.771859",
      "980-1431 S COIT RD",  "33.22217,-96.771859",
      "980-1432 S COIT RD",  "33.22217,-96.771859",
      "980-1433 S COIT RD",  "33.22217,-96.771859",
      "980-1434 S COIT RD",  "33.22217,-96.771859",
      "980-1435 S COIT RD",  "33.22217,-96.771859",
      "980-1436 S COIT RD",  "33.22217,-96.771859",
      "980-1437 S COIT RD",  "33.22217,-96.771859",

      "1777 TIMBER CREEK RD","33.04238,-97.056006"

  });

  private static final String[] MWORD_STREET_LIST = new String[]{
    "A O REEVES",
    "A RHEA MILLS",
    "ACORN HILL",
    "ALPHA OMEGA",
    "AMERICAN LEGION",
    "ANGLE RIDGE",
    "ANGUS RANCH",
    "ANNA CADE",
    "ANNIE OAKLEY",
    "ARBOR BROOK",
    "ARBOR CREEK",
    "ARBOR MANORS",
    "ARMADILLO RANCH",
    "ARNELL KELLY",
    "ASH LEAF",
    "AUDIE MURPHY",
    "AUTUMN BREEZE",
    "AUTUMN HILL",
    "AUTUMN SAGE",
    "BALMORAL CASTLE",
    "BAT MASTERSON",
    "BEACON HILL",
    "BEAR CREEK",
    "BEAVER CREEK",
    "BELLA VISTA",
    "BELT LINE",
    "BENT GRASS",
    "BENT OAK",
    "BETHEL SCHOOL",
    "BETTYE GAYE",
    "BIG BEAR",
    "BIG CANYON",
    "BIG DELTA",
    "BIG LAKE",
    "BIRD FARM",
    "BLACK MAPLE",
    "BLACK OAK",
    "BLACK WALNUT",
    "BLUE BIRD",
    "BLUE LEAF",
    "BLUE RIDGE",
    "BLUE SAGE",
    "BLUE STEM",
    "BLUE THISTLE",
    "BOB HARDY RANCH",
    "BOB TEDFORD",
    "BOB WHITE",
    "BOBBY K MARKS",
    "BOIS D ARC",
    "BOIS DE ARC",
    "BOLD RULER",
    "BONNIE VIEW",
    "BOULDER CREEK",
    "BRAMBLE BRANCH",
    "BRANDING IRON",
    "BRIAR GROVE",
    "BRIAR OAK",
    "BRIMBERRY CEMETERY",
    "BRINLEE BRANCH CREEK",
    "BRINLEE CREEK RANCH",
    "BROCKDALE PARK",
    "BROKEN BEND",
    "BROKEN SPUR",
    "BROOK HOLLOW",
    "BRUSH CREEK",
    "BRUTON ORAND",
    "BRYAN WALKER",
    "BRYANT FARM",
    "BUFFALO BILL",
    "BUFFALO SPRINGS",
    "BULL HEAD",
    "BURDEN RANCH",
    "BUSINESS PARK",
    "BUTCH CASSIDY",
    "C RHEA MILLS",
    "CACTUS PATH",
    "CADDO CREEK",
    "CANADIAN RIVER",
    "CANEY CREEK",
    "CANYON CREEK",
    "CANYON FALLS",
    "CAPE BRETT",
    "CARRIAGE HOUSE",
    "CARTER RANCH",
    "CEDAR BRANCH",
    "CEDAR COVE",
    "CEDAR CREEK",
    "CEDAR CREST",
    "CEDAR HILL",
    "CEDAR HOLLOW",
    "CEDAR LAKE",
    "CEDAR RIDGE",
    "CEDAR SPRINGS",
    "CEDAR WOOD",
    "CHALK HILL",
    "CHAMPION WOOD YARD",
    "CHAPEL SPRINGS",
    "CHASE CREEK",
    "CHERRY BLOSSOM",
    "CHERRY HILLS",
    "CHERRY WOOD",
    "CHIMNEY ROCK",
    "CHINN CHAPEL",
    "CHRISTIE FARMS",
    "CHUCK WAGON",
    "CIBOLO CREEK",
    "CITY HALL",
    "CITY LINE",
    "CITY PARK",
    "CLEAR CREEK",
    "CLEAR HAVEN",
    "CLEAR LAKE PARK",
    "CLEAR LAKE",
    "CLIFF CREEK",
    "CLOUD VIEW",
    "CLOVER RIDGE",
    "COFFEE MILL",
    "COL ETHEREDGE",
    "COLD WATER",
    "COLLIN GREEN",
    "COPEVILLE WEST",
    "CORAL RIDGE",
    "COTTAGE HILL",
    "COTTON GIN",
    "COUER DU LAC",
    "COUNTRY BROOK",
    "COUNTRY CLUB",
    "COUNTRY CREEK",
    "COUNTRY MEADOW",
    "COUNTRY RIDGE",
    "COUNTRY TIME",
    "COUNTRY WALK",
    "COUNTY LINE",
    "COYOTE CALL",
    "COYOTE CREEK",
    "CREEK CANYON",
    "CREEK CROSSING",
    "CREEK VIEW",
    "CREEK WOOD",
    "CREPE MYRTLE",
    "CRESCENT VALLEY",
    "CRESCENT VIEW",
    "CRESTED BUTTE",
    "CRIPPLE CREEK",
    "CROOKED STICK",
    "CROSS CREEK",
    "CROSS FENCE",
    "CROSS HAVEN",
    "CROSS POST",
    "CROSS TIMBERS",
    "CROSS TRAIL",
    "CROWN COLONY",
    "CRYSTAL FALLS",
    "CYPRESS BEND",
    "CYPRESS CREEK",
    "CYPRESS LAKE",
    "CYPRESS LEAF",
    "DAISY CORNER",
    "DALLAS YOUNG",
    "DARK HOLLOW",
    "DE BERRY",
    "DEER RUN",
    "DEER TRACK PARK",
    "DEL CARMEN",
    "DEL NORTE",
    "DENTON TAP",
    "DESERT CREEK",
    "DESERT WILLOW",
    "DIAMOND POINT",
    "DIPPING VAT",
    "DODGE OAKHURST",
    "DOE CREEK",
    "DOGWOOD PARK",
    "DOUBLE B",
    "DOUBLE LAKE",
    "DOUBLE R",
    "DOVE COVE",
    "DOVE HILL",
    "DOVE TAIL",
    "DRAKE HILL",
    "DUBLIN RIDGE",
    "EAGLE MONTAIN",
    "EAGLES PEAK",
    "EAST FORK",
    "EAST LAKE",
    "EAST MAIN",
    "ECHO RIDGE",
    "EDNA VALLEY",
    "EL CAMINO",
    "ELK CHASE",
    "EMI KATE",
    "ENGLISH IVY",
    "EVENING SUN",
    "FAIR OAKS",
    "FAIRWAY CROSSING",
    "FAIRWAY VIEW",
    "FARR HILL",
    "FARRIS CEMETERY",
    "FAT CAT",
    "FATE MAIN",
    "FISH HATCHERY",
    "FLOWER MOUND",
    "FOREST CREEK",
    "FOREST CREST",
    "FOREST GLEN",
    "FOREST HILL",
    "FOREST LAKE",
    "FOREST MEADOW",
    "FOREST OAKS",
    "FOREST VISTA",
    "FORREST ROSS",
    "FOSTER CROSSING",
    "FOUNTAIN VIEW",
    "FOUR NOTCH",
    "FOX TROT",
    "GARDEN RIDGE",
    "GAS HOUSE",
    "GENE AUTRY",
    "GENTLE CREEK",
    "GEORGE BUSH",
    "GLACIER POINT",
    "GLEN CANYON",
    "GLEN ELLEN",
    "GLEN LAKES",
    "GLEN RIDGE",
    "GOLDEN ROD",
    "GORDON HEIGHTS",
    "GOSPEL HILL CEMETERY",
    "GOURD CREEK CEMETERY",
    "GOURD CREEK",
    "GRAMERCY PARK",
    "GRAND HERITAGE",
    "GRANITE VALLEY",
    "GRANT COLONY CEMETERY",
    "GRASSY CREEK",
    "GREEN ACRES",
    "GREEN BRIAR",
    "GREEN MEADOW",
    "GREENWOOD MEMORIAL",
    "HADLEY CREEK",
    "HALEY HOLLOW",
    "HALF DOME",
    "HALL RANCH",
    "HANAKOA FALLS",
    "HANK BENGE",
    "HARBOR HILLS",
    "HARDY BOTTOM",
    "HARVEST CROSSING",
    "HARVEST HILL",
    "HAY MEADOW",
    "HAZY MEADOW",
    "HEATHER GLEN",
    "HELMOKEN FALLS",
    "HICKORY CREEK",
    "HICKORY HILL",
    "HIDDEN BLUFF",
    "HIDDEN BROOK",
    "HIDDEN COVE",
    "HIDDEN CREEK",
    "HIDDEN FALLS",
    "HIDDEN GLEN",
    "HIDDEN LAKE",
    "HIDDEN VALLEY",
    "HIGH MEADOW",
    "HIGH PLAINS",
    "HIGH POINT",
    "HIGH RIDGE",
    "HIGH WILLOW",
    "HIGHLAND FAIRWAY",
    "HIGHLAND MEADOW",
    "HIGHLAND MEADOWS",
    "HIGHLAND RIDGE",
    "HIGHLAND VILLAGE",
    "HIGHRIDGE FARMS",
    "HILL PARK",
    "HILL TOP",
    "HILL VIEW",
    "HITCHING POST",
    "HOLLY LEAF",
    "HONEY CREEK",
    "HONEY LOCUST",
    "HONEY MESQUITE",
    "HOOT OWL",
    "HORACE SMITH",
    "HORDS CREEK",
    "HORSESHOE LAKE",
    "HUNTERS GLEN",
    "HUNTERS RIDGE",
    "HUNTERS RUN",
    "INDIAN BLANKET",
    "INDIAN ROCK",
    "J C WALKER",
    "J H MASSEY",
    "J R",
    "JACK PATE",
    "JERRY WAYNE COMBEST",
    "JOE SMITH",
    "JOE WERNER",
    "JOHN CAMPBELL",
    "JONES VIEW",
    "JOURNEY FORTH",
    "JULIA JUSTICE",
    "KATE BOLDEN",
    "KINGS POINT",
    "KNOTTY PINE",
    "L TAYLOR",
    "LA CIMA",
    "LAFONS FARM",
    "LAKE BLUFF",
    "LAKE BREEZE",
    "LAKE CREST",
    "LAKE FALLS",
    "LAKE FOREST",
    "LAKE HARRISON",
    "LAKE LAND PARK",
    "LAKE PARK",
    "LAKE PLACE",
    "LAKE SHADOW",
    "LAKE SHORE",
    "LAKE TRAIL",
    "LAKE TRAILS",
    "LAKE VIEW",
    "LAKE VISTA",
    "LAKE WICHITA",
    "LAKE WOOD",
    "LANDIS LAKE",
    "LAUREL OAK",
    "LAUREL SPRINGS",
    "LAVON DAM",
    "LAVON VIEW",
    "LAVON VILLAS",
    "LAZY BEND",
    "LEE CEMETERY",
    "LEE HUDSON",
    "LENORARD WAY",
    "LEWIS CANYON",
    "LIGHT FARMS",
    "LINCOLN WOOD",
    "LITTLE LAKE",
    "LITTLE RANCH",
    "LIVE OAK",
    "LOGANS WAY",
    "LOMA ALTA",
    "LONE OAK",
    "LONE RIDGE",
    "LONE STAR",
    "LONESOME DOVE",
    "LONG POND",
    "LONG PRAIRIE",
    "LONG VALLEY",
    "LONGSTREET CEMETERY",
    "LOOK OUT",
    "LOST CREEK",
    "LOUIS DAVIS",
    "LOUIS VOAN",
    "LOVE BIRD",
    "LUCAS BRANCH",
    "LUCKY 13",
    "MALLARD PARK",
    "MAPLE LEAF",
    "MAPLE RIDGE",
    "MARBLE PASS",
    "MARK ALEXANDER",
    "MARTHA CHAPEL CEMETERY",
    "MARTIN CREEK",
    "MARTIN LUTHER KING JR",
    "MARY ANN",
    "MARY RUTH",
    "MATHIS DAIRY",
    "MAXWELL CREEK",
    "MCCARLEY RANCH",
    "MCKAMY CREEK",
    "MEADOW CREEK",
    "MEADOW CREST",
    "MEADOW GLEN",
    "MEADOW GREEN",
    "MEADOW HILL",
    "MEADOW LAKE",
    "MEADOW LARK",
    "MEADOW PARK",
    "MEADOW RIDGE",
    "MEADOW RUN",
    "MEADOW VIEW",
    "MEADOW VISTA",
    "MEADOW WOOD",
    "MEADOW WOODS",
    "MEANDERING CREEK",
    "MEDICAL CENTER",
    "MEDICAL PARK",
    "MEDICAL PLAZA",
    "MELISSA LANDFILL",
    "MEMORIAL HOSPITAL",
    "MIKE BETH",
    "MIKE SLOTT",
    "MILL BRANCH",
    "MILLERS CREEK",
    "MISSION CREEK",
    "MISTY WAY",
    "MOFFETT SPRINGS",
    "MONTE CARLO",
    "MOORLAND PASS",
    "MORNING VIEW",
    "MOSS CREEK",
    "MOSS GLEN",
    "MOSSY OAK",
    "MOSSY OAKS",
    "MT ZION CHURCH",
    "MT ZION",
    "MURRELL PARK",
    "MUSTANG RIDGE",
    "NATCHES TRACE",
    "NIAGARA FALLS",
    "NORMAL PARK",
    "NORTH GRAPEVINE MILL",
    "NORTH HILL",
    "NORTH RIDGE",
    "NORTH STAR",
    "OAK BEND",
    "OAK BLUFF",
    "OAK CREEK",
    "OAK DELL",
    "OAK GLEN",
    "OAK GROVE",
    "OAK HILL",
    "OAK HOLLOW",
    "OAK LAWN",
    "OAK PARK",
    "OAK SPRINGS",
    "OAK TRAIL",
    "OBANNON RANCH",
    "OLD CROSS TIMBERS",
    "OLDE MILL",
    "OLDE OAKS",
    "OLIVE BRANCH",
    "ONE PLACE",
    "ONLY ONE",
    "OUTER LOOP",
    "OYSTER BAYOU",
    "PACIFIC PEARL",
    "PAINT CREEK",
    "PALO DURO",
    "PALO PINTO",
    "PARK BEND",
    "PARK MEADOW",
    "PARK RIDGE",
    "PARK TRAILS",
    "PARK VALLEY",
    "PARK VISTA",
    "PARKER SQUARE",
    "PAT HENRY CEMETERY",
    "PAT KELLY",
    "PAUL DIXON",
    "PAUL MICHAEL",
    "PEACH TREE",
    "PEBBLE BROOK",
    "PEBBLE CREEK",
    "PECAN CREEK",
    "PECAN GROVE",
    "PECAN HOLLOW",
    "PECAN MEADOWS",
    "PECAN PARK",
    "PECAN PLACE",
    "PENTON LINNS",
    "PERCY HOWARD",
    "PETERS COLONY",
    "PHEASANT CREEK",
    "PHEASANT RUN",
    "PHELPS SLAB",
    "PHIL WOOD",
    "PILOT POINT",
    "PIN OAK",
    "PINE GULLY",
    "PINE HOLLOW",
    "PINE HURST",
    "PINE KNOLL",
    "PINE KNOT",
    "PINE LAKE",
    "PINE NEEDLE",
    "PINE PRAIRIE SCHOOL",
    "PINE RIDGE",
    "PINE SHADOWS",
    "PINE TOP",
    "PINE TREE",
    "PINE VALLEY",
    "PINEY POINT",
    "PIONEER PARK",
    "PIONEER PATH",
    "PLEASANT RUN",
    "PLEASANT VALLEY",
    "PLUM TREE",
    "PLYMOUTH COLONY",
    "POINT DE VUE",
    "POINT WEST",
    "PONDEROSA PINE",
    "PONY EXPRESS",
    "PORT OCONNER",
    "POST CREST",
    "POST OAK",
    "PRAIRIE CREEK",
    "PRAIRIE VISTA",
    "PRESTON COUNTRY",
    "PRESTON HILLS",
    "PRESTON LAKES",
    "PRINCETON MEADOWS",
    "PRINCETON OAKS",
    "PROSPER COMMONS",
    "PUEBLO VIEJO",
    "PUNK CARTER",
    "PURPLE MARTIN",
    "PURPLE SAGE",
    "QUAIL CREEK",
    "QUAIL HOLLOW",
    "QUAIL RIDGE",
    "QUAIL RUN",
    "QUARTER HORSE",
    "RANCH ACRES",
    "RANCH ROAD",
    "RANCHO VISTA",
    "RAVEN TERRACE",
    "RAVENWOOD VILLAGE",
    "RAY BLACK",
    "RED BUD",
    "RED DEER",
    "RED MAPLE",
    "RED OAK",
    "RED PINE",
    "RED WING",
    "REGENCY PARK",
    "REMINGTON PARK",
    "RENFRO VALLEY",
    "RHEA MILLS",
    "RIDGE VIEW",
    "RING TEAL",
    "RIPPLE CREEK",
    "RIVER BIRCH",
    "RIVER OAKS",
    "RIVER PLACE",
    "RIVER WALK",
    "ROBINSON CREEK",
    "ROCK CANYON",
    "ROCK CLIFF",
    "ROCK HILL",
    "ROCK RIDGE",
    "ROCKY FORD",
    "ROCKY MOUNTAIN",
    "ROLLING BROOK",
    "ROLLING HILLS",
    "ROLLING MEADOW",
    "ROLLING OAK",
    "ROLLING RIDGE",
    "ROSE RANCH",
    "ROUND PRAIRIE",
    "ROY ROGERS",
    "ROY WEBB",
    "ROYAL GLEN",
    "ROYAL OAK",
    "RUBY CREST",
    "RUNNING BROOK",
    "RUNNING DEER",
    "RYANS FERRY",
    "SADDLE CLUB",
    "SADDLE CREEK",
    "SADDLE HORN",
    "SADDLE OAK",
    "SADDLE RIDGE",
    "SAINT PAUL",
    "SALMON LAKE",
    "SAM HOUSTON",
    "SAM RAYBURN",
    "SAM SLOTT",
    "SAN JACINTO",
    "SANDY CREEK FARM",
    "SANDY LAKE",
    "SANTA FE",
    "SAW MILL",
    "SCALES RANCH",
    "SCENIC POINT",
    "SCHOONER BAY",
    "SENTINEL OAK",
    "SETTLERS RIDGE",
    "SHADE TREE",
    "SHADOW HILL",
    "SHADOW ROCK",
    "SHADY BEND",
    "SHADY CREEK",
    "SHADY HILL",
    "SHADY KNOLL",
    "SHADY OAK",
    "SHADY OAKS",
    "SHADY TIMBERS",
    "SID NELSON",
    "SILVER RIDGE",
    "SINGING BROOK",
    "SLATER CREEK",
    "SLEEPY HOLLOW",
    "SMITH ACRES",
    "SMITH HILL",
    "SMITHERS FARM",
    "SOC COTTON",
    "SOUTH BROADWAY",
    "SOUTH PARK",
    "SOUTHERN HILLS",
    "SOUTHWOOD FOREST",
    "SPANISH OAK",
    "SPRING CIRCLE",
    "SPRING CREEK",
    "SPRING CREST",
    "SPRING MEADOW",
    "SPRING RIDGE",
    "SPRING RUN",
    "ST ANDREWS",
    "ST CHARLES",
    "ST MARK",
    "ST PETER",
    "STANLEY FALLS",
    "STARK PLACE",
    "STEP TRAIL",
    "STERLING BROWN",
    "STERLING CHAPEL",
    "STILL FOREST",
    "STILL MEADOW",
    "STONE BEND",
    "STONE CREEK",
    "STONE CREST",
    "STONE HILL FARMS",
    "STONE TRACE",
    "STONE TRAIL",
    "STONEY POINT",
    "STUBBLEFIELD LAKE",
    "SUGAR HILL",
    "SUGAR VALLEY",
    "SUMMER STAR",
    "SUN MEADOW",
    "SUN RIDGE",
    "SUN VALLEY",
    "SUNNY CREST",
    "SUNNY HILL",
    "SUNNY KNOLL",
    "SURREY ESTATES",
    "SURREY WOODS",
    "SUTTERS MILL",
    "SUTTON FIELDS",
    "SWEET GUM",
    "TALL TIMBERS",
    "TANUR CASCADE",
    "TEA GARDEN",
    "TEA TREE",
    "TERRACE MANOR",
    "THE COWBOY",
    "THE CROSSINGS",
    "THISTLE HILL",
    "THOMAS LAKE",
    "THORN APPLE",
    "THOUSAND OAKS",
    "THREE NOTCH",
    "THREE RIVERS",
    "TIMBER CREEK",
    "TIMBER MEADOW",
    "TIMBER RIDGE",
    "TIMBER VALLEY",
    "TIN ROOF",
    "TOLEDO BEND",
    "TOM CLEVINGER",
    "TOWN CENTER",
    "TOWNE VIEW",
    "TOWNLY RANCH",
    "TRAIL RIDGE",
    "TRAILS END",
    "TRAILS PLACE",
    "TREE LINE",
    "TWIN CREEK",
    "TWIN CREEKS",
    "TWIN HILLS",
    "TWIN KNOLL",
    "TWIN LAKES",
    "TWIN OAKS",
    "TWIN VALLEY",
    "VALLEY DALE",
    "VALLEY OAK",
    "VALLEY RIDGE",
    "VALLEY STREAM",
    "VALLEY VIEW",
    "VAN ZANDT",
    "VETERANS MEMORIAL",
    "VIA ITALIA",
    "VICK SPRING",
    "VICTORIA FALLS",
    "VILLAGE CREEK",
    "VILLAGE CREST",
    "VIRTUE PORT",
    "VISTA VIEW",
    "WAGON WHEEL",
    "WALNUT LAKE",
    "WALNUT RIDGE",
    "WASHINGTON POST",
    "WATCH HILL",
    "WATER OAK",
    "WATERS EDGE",
    "WATERSTONE ESTATES",
    "WATSON LAKE",
    "WAVERLY CEMETERY",
    "WESLEY GROVE",
    "WEST CROSSING N",
    "WEST CROSSING",
    "WEST OAK",
    "WEST WALNUT",
    "WESTON CREEK",
    "WHISPER CREEK",
    "WHISPERING HILLS",
    "WHISPERING MEADOWS",
    "WHISPERING OAKS",
    "WHISPERING PINE",
    "WHISTLING DUCK",
    "WHITE BUD",
    "WHITE CHALK",
    "WHITE CLOVER",
    "WHITE CREST",
    "WHITE MOUNTAIN",
    "WHITE OAK",
    "WHITE PINE",
    "WHITE RIVER",
    "WHITE TAIL",
    "WHITLEY PLACE",
    "WILD ROSE",
    "WILLIAM THOMAS",
    "WILLIS OSBURN",
    "WILLOW CREEK",
    "WILLOW PLACE",
    "WILLOW POINT",
    "WILLOW RIDGE",
    "WILLOW RUN",
    "WILLOW SPRINGS",
    "WILLOW TREE",
    "WILLOW WOOD",
    "WILSON CREEK",
    "WIND BROOK",
    "WINDING CREEK",
    "WINDING OAKS",
    "WINDING RIVER",
    "WINDSOR CENTRE",
    "WINDY KNOLL",
    "WIRE ROAD",
    "WISDOM CREEK",
    "WOLF CREEK",
    "WOLF RUN",
    "WOOD CREEK",
    "WOOD FARM",
    "WOOD LODGE",
    "WOODED CREEK",
    "WYATT EARP",
    "WYLIE EAST",
    "WYNDHAM MEADOWS"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "10-100 THEFT",
      "10-150 MAJOR ACCIDENT",
      "1050 MAJOR",
      "1050 MINOR",
      "2ND PAGE",
      "911 HANG UP/OPEN LINE",
      "AAST",
      "AALARM",
      "ABANDONED CHILD",
      "ABANDONED VEHICLE",
      "ABNORMAL BREATHING",
      "ABDOMINAL PAIN",
      "ALARM",
      "ALARM CALL",
      "ALLERGIES/ENVENOMATIONS",
      "AMB",
      "AMBULANCE/EMS SERVICE",
      "ANIMAL",
      "ANIMAL BITE",
      "ANIMAL COMPLAINT",
      "APUBLIC",
      "ADDITIONAL MANPOWER",
      "ASSAULT",
      "ASSAULT JUST OCCURRED",
      "ASSIST MOTORIST",
      "ASSIST OTHER AGENCY",
      "ASSIST PD",
      "ASSIST POLICE DEPARTMENT",
      "ASSAULT REPORT",
      "ASSLT2",
      "ASUICI",
      "ATTEMPT SUICIDE",
      "ATTEMPTED SUICIDE",
      "AUDIBLE BURGLAR ALARM",
      "AUTOMATIC AID ENGINE",
      "AUTOMATIC FIRE ALARM",
      "BACK PAIN",
      "BASST",
      "BITE",
      "BOAT ASSIST",
      "BOAT HAZARD",
      "BOAT IN DISTRESS",
      "BOATING ACCIDENT",
      "BREATHING PROBLEMS",
      "BRUSH TRUCK REQUESTED",
      "BURGLARY MOTOR VEH IN PROGRESS",
      "BURGLARY REPORT",
      "BURN",
      "CAR/VEHICLE FIRE",
      "CARBON MONOXIDE ALARM",
      "CARBON MONOXIDE INVESTIGATION",
      "CARDIAC ARREST / DEATH",
      "CFALARM",
      "CHASE",
      "CHECK",
      "CHECK --- 2ND PAGE",
      "CHEMICAL FIRE",
      "CHEST PAINS",
      "CITIZEN CONTACT",
      "CIVIL",
      "CIVIL PROBLEM",
      "CIVIL STANDBY",
      "CHOKING",
      "CLOSE PATROL",
      "COMM VIDEO ALARM",
      "COMMERCIAL FIRE",
      "COMERCIAL FIRE ALARM",
      "COMMERCIAL FIRE ALARM",
      "CONTROLLED BURN",
      "CONVULSIONS/SEIZURES",
      "CPR IN PROGRESS",
      "CRIMINAL TRESPASS",
      "CS",
      "DECEASED PERSON",
      "DDIST",
      "DIABETIC PROBLEMS",
      "DISREGARD",
      "DISREGARD PAGE",
      "DISREGARD PER ALARM CO",
      "DISREGARD PER ALARM COMPANY",
      "DISREGARD PER ALARM COMPANY FALSE ALARM",
      "DISREGARD PER FVFD",
      "DIST",
      "DISTURBANCE",
      "DOCUMENTATION PURPOSE",
      "DOMESTIC",
      "DOMESTIC DISTURBANCE",
      "DOMESTIC IN PROGRESS",
      "DOUBLE OAK MEDIC TO LOCATION",
      "DOWN",
      "DOWN POWER LINE",
      "DOWN TREE",
      "DROWN",
      "DROWNING ON THE LAKE",
      "DUMPSTER FIRE",
      "DRIVING WHILE INTOXICATED",
      "DROWNING",
      "EFIRE",
      "ELECTRICAL FIRE",
      "ELECTRICAL HAZARD",
      "ELEVATOR ALARM",
      "ELEVATOR RESCUE",
      "ELEVATOR RESCUE/ALARM",
      "EMERGENCY LOCKOUT",
      "EMERGENCY MEDICAL CALL",
      "EMERGENCY MEDICAL ALARM",
      "EMERGENCY PUBLIC ASSIST",
      "EMERGENCY WATER CUTOFF",
      "EMERGENCY WATER CUT OFF",
      "EMS",
      "EMS-CARDIAC ARREST",
      "EMS-NON EMERGENCY TRANSFER",
      "EMS-SICK - INFECT. CONTROL",
      "EMS - CARDIAC ARREST",
      "EMS - CARDIAC EMERGENCY",
      "EMS - INJURED PERSON",
      "EMS - PERSON FALLEN",
      "EMS - SEIZURE",
      "EMS - SICK PERSON",
      "EMS - STROKE (CVA)",
      "EMS - SUICIDAL PERSON",
      "EMS - UNCONSCIOUS PERSON",
      "EMS CALL",
      "EMS CARDIAC",
      "EMS INFECTION",
      "EMS MEDICAL",
      "EMS OTHER/STANDBY/PR",
      "EMS TRANSFER",
      "EMS TRAUMA",
      "EMSFALL",
      "ENRT TO STAGE FOR POSSIBLE MENTAL SUBJ",
      "ENTRAPMENTS",
      "EVENT",
      "EXPLODE",
      "EXPLOSION",
      "FAIL TO LEAVE ID, ACCIDENT",
      "FAINTING/ALERT <35",
      "FALARM",
      "FALARM ****DISREGARD****",
      "FALL O/6FT",
      "FALL U/6FT",
      "FALLS",
      "FD ASST. TRAFFIC CONTROL",
      "FD MISC. FIRE",
      "FDALMRES",
      "FDBOTTOW",
      "FDLAKE",
      "FDMUTMVA",
      "FDMUTUAL",
      "FDSTRUC",
      "FIELD TEST ADULT PROB",
      "FIGHT IN PROGRESS",
      "FIRE ALARM",
      "FIRE ALARM - FROM RESIDENT",
      "FIRE ALARM TEST",
      "FIRE ARM COMPLAINT",
      "FIRE OTHER",
      "FIRE PUBLIC ASSIST",
      "FIRE REPORTED OUT",
      "FIRE TEST CALL",
      "FIREWORKS COMPLAINT",
      "FIRST",
      "FIRST RESPONDERS",
      "FIRST RESPONDERS - ASSIST OTHER AGENCY - CUSTOMER DISTURBANCE",
      "FISRT RESPONDERS",
      "FLOOD",
      "FLOODING REPORTED",
      "FOLLOW UP INVESTIGATION",
      "FOUND CHILD",
      "FOUND PROPERTY",
      "FUEL",
      "FUEL SPILL GAS LEAK",
      "FUNERAL ESCORT",
      "GFIRE",
      "GRASS FIRE",
      "GRASS/BRUSH FIRE",
      "GRASS/BRUSH FIRE (LOW HAZD)",
      "GRASS/WILDLAND FIRE",
      "GRASS OR BRUSH FIRE",
      "GREASE FIRE",
      "GRILL FIRE",
      "HAZARD",
      "HAZARDOUS CONDITION",
      "HAZARDOUS MATERIALS",
      "HAZMAT SPILL/LEAK",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE/LACERATIONS",
      "HMAJOR",
      "HMINOR",
      "HURT",
      "INDOOR NATURAL GAS LEAK",
      "INJURED ANIMAL",
      "INJURED PERSON",
      "INJURY",
      "INTOXICATED DRIVER COMPLAINT",
      "INVESTIGATION",
      "INVESTIGATION-UNKNOWN SIT.",
      "JUVENILE",
      "JUVENILE PROBLEMS",
      "LAKE EMERGENCY",
      "LEAK",
      "LIFT ASSIST",
      "LOST OR MISSING CHILD",
      "LIFT ASSIST ONLY",
      "LOCK-IN/OUT",
      "LOCKED",
      "LOCKED VEHICLE/RESIDENCE",
      "LOCKOUT",
      "LOCKOUT - NON EMERGENT",
      "LOOSE",
      "LOOSE LIVESTOCK",
      "MAJ ACCIDENT W/RESCUE (TONE)!!",
      "MAJOR",
      "MAJOR ACCIDENT",
      "MAJOR ACCIDENT (INJURIES)",
      "MAJOR ACCIDENT (MAJOR ROAD)",
      "MAJOR ACCIDENT (TONE) !!",
      "MAJOR ACCIDENT 10/50",
      "MAJOR ACCIDENT WITH MOTORCYCLE",
      "MAJOR HIT AND RUN ACCIDENT",
      "MAJOR WITH ENTRAPMENT",
      "MAJOR WITH EXTRICATION",
      "MALARM",
      "MALARM -- DISREGARD",
      "MANUAL PAGE",
      "MASST",
      "MINOR",
      "MINOR ACCIDENT",
      "MINOR ACCIDENT 10/50",
      "MINOR HIT AND RUN ACCIDENT",
      "MINOR NOW MAJOR",
      "MISCELLANEOUS FIRE",
      "MISSING CHILD",
      "MEDIC CALL- COALITION",
      "MEDICAL ALARM",
      "MEDICAL EMERGENCY",
      "MEDICATION OVERDOSE",
      "MENTAL",
      "MENTAL -- NO SIGN NO SIRENS --",
      "MENTAL SUBJECT",
      "MENTALLY ILL PERSON",
      "MISCELLANEOUS FIRE",
      "MISSING PERSON",
      "MOTORIST ASSIST",
      "MP",
      "MUTUAL",
      "MUTUAL AID",
      "MUTUAL AID-FIRE",
      "MUTUAL AID FIRE",
      "MUTUAL AID,FIRE/FILL IN",
      "MUTUAL AID AMB FOR MAJOR",
      "MUTUAL AID AMBULANCE",
      "MUTUAL AID BRUSH",
      "MUTUAL AID ENGINE",
      "MUTUAL AID ENGINE FILL IN",
      "MUTUAL AID ENGINE TO SCENE",
      "MUTUAL AID FILL IN",
      "MUTUAL AID FOR GRASS FIRE",
      "MUTUAL AID FVFD",
      "MUTUAL AID FIRE/RESCUE REQUEST",
      "MUTUAL AID GRASS FIRE",
      "MUTUAL AID, MEDIC TO LOCATION",
      "MUTUAL AID MEDICAL CALL",
      "MUTUAL AID MEDICAL EMERGENCY",
      "MUTUAL AID MVA",
      "MUTUAL AID ON SFIRE",
      "MUTUAL AID OTHER",
      "MUTUAL AID RECALL",
      "MUTUAL AIDE REQ TENDER",
      "MUTUAL AID SFIRE",
      "MUTUAL AID STRUCTURE FIRE",
      "MUTUAL AID TRUCK TO THE SCENE",
      "MUTUAL MUTUAL",
      "MVA2",
      "NARC",
      "NARCOTICS INVESTIGATION",
      "NATURAL / PROPANE GAS LEAK",
      "NATURAL DISASTER",
      "NATURAL GAS LEAK",
      "NATURAL GAS LEAK - INDOORS",
      "NCHILD",
      "NOISE DISTURBANCE",
      "NOT DANGEROUS BODY AREA",
      "ODOR",
      "ODOR INSIDE STRUCTURE",
      "ODOR INVESTIGATION",
      "ODOR/SMOKE INVESTIGATION",
      "ONLINE HARASSMENT",
      "OPEN DOOR/BUILDING",
      "OPEN LINE",
      "OTHER CALL FOR FIRE DEPARTMENT",
      "OTHER CALL FOR POLICE",
      "OUTSIDE FIRE",
      "OUTSIDE NATURAL GAS LEAK",
      "OVERDOSE",
      "PANIC ALARM",
      "PASSED",
      "PATIENT ASSIST OR LIFT ASSIST",
      "PERSON",
      "PHONE HARASSMENT REPORT",
      "POSS SIG 27",
      "PROPERTY PUBLIC ASSIST",
      "PROTECTION ORDER",
      "PSYCHOLOGICAL/ABNORMAL BEH",
      "PUBLIC ASSIST",
      "PUBLIC ASSIST - APUBLIC",
      "PUBLIC ASSIST (FD)",
      "PUBLIC ASSISTANCE",
      "PUBLIC INTOX",
      "QUINT",
      "RECKLS",
      "RECKLESS DRIVER",
      "RES. SMOKE DETECTOR PROBLEM",
      "RESCUE",
      "RESCUE-TRAPED PERSON(S)",
      "RESCUE-TRAPPED PERSON(S)",
      "RESD. FIRE ALARM",
      "RESIDENTIAL FIRE",
      "RESIDENTIAL FIRE ALARM",
      "RESIDENTIAL PANIC ALARM",
      "ROAD RAGE",
      "RUNAWAY REPORT",
      "SEARCH FOR MISSING PERSON",
      "SEXUAL ASSAULT OF A CHILD",
      "SFIRE",
      "SFIRE / MUTUAL AID",
      "SHOOTING",
      "SHOOTING/STABBING",
      "SICK PERSON",
      "SMOKE",
      "SMOKE DETECTOR ALARM",
      "SMOKE INVESTIGATION",
      "SOLICITOR COMPLAINT",
      "SPECIAL ASSIGNMENT",
      "SPECIAL ASSIGNMENT \"SPECIFY\"",
      "SPECIAL EVENT",
      "SPECIAL HAZARD",
      "SPECIAL WATCH",
      "SPEEDING VEHICLE",
      "SPEAK WITH OFFICER",
      "STAB",
      "STANDBY ELECTRICAL FIRE",
      "STILL ALARM ENGINE",
      "STROKE",
      "STRUCTURE FIRE",
      "SUBJECT PASSED OUT",
      "SUICIDAL PERSON",
      "SUICIDE THREAT",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "TASST",
      "TEST CALL",
      "TEST FIRE & EMS CALL",
      "TEST FIRE CALL",
      "TFIRE",
      "THEFT IN PROGRESS",
      "THEFT REPORT",
      "THREATS",
      "TRAFFIC ASSIST",
      "TRAFFIC COMPLAINT",
      "TRAFFIC HAZARD",
      "TRAFFIC STOP",
      "TRAIL RESCUE FOR FD",
      "TRANSFORMER FIRE",
      "TRASH / DUMPSTER FIRE",
      "TRASH FIRE",
      "TRASH FIRE / DUMPSTER FIRE",
      "TRAUMATIC INJURIES",
      "TREE",
      "TS",
      "TSUICI",
      "UNK",
      "UFIRE",
      "UNAUTHORIZED BURN",
      "UNAUTHORZED BURN",
      "UNCONSCIOUS PERSON",
      "UNLOCK REQUEST",
      "UTILITY LINES DOWN",
      "UNATTENDED DEATH",
      "UNKNOWN MEDICAL PROBLEM",
      "UNKNOWN FIRE",
      "VANDALISM JUST OCCURRED",
      "VANDALISM/CRIM MISCHIEF REPORT",
      "VEHICLE / RESIDENCE",
      "VEHICLE DISTURBANCE",
      "VEHICLE FIRE",
      "VEHICLE THEFT REPORT",
      "VFIRE",
      "VFIRE -- DISREGARD PER CALLER FIRE OUT",
      "WATER",
      "WATER FLOW ALARM",
      "WATER FLOW ALARM, BUSN OR RESD",
      "WATER LEAK",
      "WATER LEAK - MINOR",
      "WATER RESCUE",
      "WELFARE CHECK",
      "WELFARE CONCERN",
      "WIRES DOWN"
  );

  private static final CodeSet DOUBLE_CITY_LIST = new CodeSet(
    "BLUE RIDGE",
    "COLLIN COUNTY",
    "COLLIN CO",
    "FIRST - 2ND PAGE",
    "FLOWER MOUND",
    "GRAYSON COUNTY",
    "GRAYSON CO",
    "HUNT COUNTY",
    "HUNT CO",
    "LITTLE ELM",
    "LOWRY CROSSING",
    "MC KINNEY",
    "NEW WAVERLY",
    "PILOT POINT",
    "ROYSE CITY",
    "SAINT PAUL",
    "ST PAUL",
    "VAN ALSTYNE",
    "WALKER COUNTY"
  );
}
