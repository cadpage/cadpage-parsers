package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ILKaneCountyCParser extends SmartAddressParser {

  public ILKaneCountyCParser() {
    super(ILKaneCountyParser.CITY_LIST, "KANE COUNTY", "IL");
    setFieldList("CALL ADDR APT CITY PLACE X INFO GPS");
    setupCallList(CALL_LIST);
    setupSaintNames("CHARLES");
    setupMultiWordStreets(
        "ABBEY GLEN",
        "ARBOR CREEK",
        "ARMY TRAIL",
        "BAR HARBOR",
        "BARB HILL",
        "BAY SHORE",
        "BIG TIMBER",
        "BILLY BURNS",
        "BOWES BEND",
        "BREWSTER CREEK",
        "BRIDLE CREEK",
        "BRIER HILL",
        "CAMPTON HILLS",
        "CAMPTON WOOD",
        "CAPE COD",
        "CARL LEE",
        "CLOVER FIELD",
        "COUNTRY CLUB",
        "COUNTY LINE",
        "CREEK VIEW",
        "CURLING POND",
        "DEER RUN",
        "DIAMOND HEAD",
        "DUNHAM TRAILS",
        "EAST LAURA INGALLS WILDER",
        "EAST MARY",
        "EDGAR LEE MASTERS",
        "EDNA ST VINCENT MILLAY",
        "EMILY DICKINSON",
        "FERSON CREEK",
        "FOX BEND",
        "FOX BLUFF",
        "FOX MILL",
        "FOX RIVER",
        "FOX RUN",
        "FRANCIS BRET HARTE",
        "GLEN COVE",
        "GREY BARN",
        "HAPPY HILLS",
        "HIDDEN HILL",
        "HIDDEN LAKES",
        "HIDDEN OAKS",
        "HOMEWARD HILL",
        "JJC LINCOLN",
        "JOYCE KILMER",
        "LAKE BLUFF",
        "LONG VIEW",
        "LOON LAKE",
        "NORTH JAMES",
        "OAK RIDGE",
        "OLIVER WENDELL HOLMES",
        "PADRE ISLAND",
        "PINE HILLS",
        "PINE HILLS",
        "PRAIRIE HILL",
        "PRAIRIE LAKES",
        "RAMM WOODS",
        "RED BARN",
        "RED GATE",
        "RED HAWK",
        "RED LEAF",
        "RICHARD J BROWN",
        "RIDGE LINE",
        "RIVER GRANGE",
        "RIVERSIDE",
        "ROLLING OAKS",
        "SETTLERS GROVE",
        "SILVER GLEN",
        "SILVER LAKE",
        "SORRENTOS MCGOUGH",
        "TALL PINES",
        "TAYLOR CALDWELL",
        "TIMBER RIDGE",
        "TOMS TRAIL",
        "TOWN PLACE",
        "VACHEL LINDSAY",
        "VALLEY STREAM",
        "WALT WHITMAN",
        "WEST LAURA INGALLS WILDER",
        "WEST MARY",
        "WEST VIEW",
        "WHISPERING WILLOWS",
        "WHITE PINE",
        "WILD ROSE",
        "WILLIAM CULLEN BRYANT",
        "WINDING HILL"
    );
  }

  @Override
  public String getFilter() {
    return "shfradio@co.kane.il.us";
  }

  private static final Pattern DIR_STREET_NO_PTN = Pattern.compile(" (\\d+[NSEW]\\d? ?\\d+) ");
  private static final Pattern MIXED_CASE_PTN = Pattern.compile("[A-Z][a-z].*");
  private static final Pattern DIR_STREET_NO_PTN2 = Pattern.compile("(\\d+[NSEW]\\d? \\d+) ");
  private static final Pattern X_ST_PTN1 = Pattern.compile("([. A-Z0-9]+ ?/ ?[. ,A-Z0-9]+?)(?: {2,}|$)");
  private static final Pattern X_ST_PTN2 = Pattern.compile("([. A-Z0-9]+ ?/ ?[.A-Z0-9]+?)(?: |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!")) return false;

    // They have some weird street numbers that throw the smart address parser
    // for a loop.  So check for them first.
    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    String dirStreetNo = null;
    Matcher match = DIR_STREET_NO_PTN.matcher(body);
    if (match.find()) {
      String call = body.substring(0,match.start()).trim();
      Result tmpRes = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, call);
      String city = tmpRes.getCity();
      if (!MIXED_CASE_PTN.matcher(city).matches()) {
        st = StartType.START_ADDR;
        flags = 0;
        data.strCall = call;
        dirStreetNo = match.group(1);
        body = "999999" + body.substring(match.end(1));
      }
    }

    else {
      int pt = body.indexOf(" <UNKNOWN> ");
      if (pt >= 0) {
        data.strCall = body.substring(0,pt).trim();
        data.strAddress = "<UNKNOWN>";
        data.strSupp = body.substring(11).trim();
        return true;
      }
    }

    // They like verbose highway names
    body = body.replace(" UNITED STATES HIGHWAY ", " US ");
    body = body.replaceAll(" ILLIONIS ", " ILLINOIS ");
    body = body.replace(" ILLINOIS ROUTE ", " IL ");

    match = GPS_PATTERN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(), data);
      data.strSupp = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    }


    // Lets see what we can do...
    parseAddress(st, flags | FLAG_IMPLIED_INTERSECT | FLAG_IGNORE_AT| FLAG_CROSS_FOLLOWS | FLAG_RECHECK_APT, body, data);
    if (!isValidAddress() && data.strCity.length() == 0) return false;
    body = getLeft();
    if (dirStreetNo != null && data.strAddress.startsWith("999999 ")) {
      data.strAddress = dirStreetNo + data.strAddress.substring(6);
    }

    // Next is the cross street. But it is a bit chancy.  Let's see what we can do
    // with this.
    do {

      if (body.startsWith("No Cross Streets Found ")) {
        data.strSupp = body.substring(23).trim();
        break;
      }

      match = X_ST_PTN1.matcher(body);
      if (match.lookingAt()) {
        data.strCross = match.group(1).trim();
        data.strSupp = body.substring(match.end()).trim();
        break;
      }

      match = X_ST_PTN2.matcher(body);
      if (match.lookingAt()) {
        data.strCross = match.group(1).trim();
        data.strSupp = body.substring(match.end()).trim();
        break;
      }

      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_CITY, body);
      if (res.isValid()) {
        res.getData(data);
        data.strSupp = res.getLeft();
        if (data.strSupp.length() == 0) {
          data.strSupp = data.strCross;
          data.strCross = "";
        }
        break;
      }

      data.strSupp = body;

    } while (false);

    // Split out place name from cross street
    String cross = data.strCross;
    data.strCross = "";
    String crossExt = null;
    int pt = cross.indexOf(',');
    if (pt >= 0) {
      crossExt = cross.substring(pt);
      cross = cross.substring(0, pt).trim();
    }
    parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END | FLAG_NO_CITY, cross, data);
    if (data.strCross.length() == 0) {
      data.strPlace = "";
      data.strCross = cross;
    }
    if (crossExt != null) data.strCross += crossExt;

    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = DIR_STREET_NO_PTN2.matcher(addr);
    if (match.lookingAt()) {
      addr = match.group(1).replace(" ", "") + addr.substring(match.end(1));
    }
    while (addr.startsWith("0")) addr = addr.substring(1).trim();
    return addr;
  }


  private static final CodeSet CALL_LIST = new CodeSet(
      "AMBULANCE CALL",
      "CALLBACK",
      "CARBON MONOXIDE DETECTOR",
      "FIELD FIRE",
      "FIRE ALARM",
      "FIRE CALL",
      "MABAS",
      "MUTUAL AID REQUEST",
      "STRUCTURE FIRE",
      "VEHICLE FIRE"
  );
}
