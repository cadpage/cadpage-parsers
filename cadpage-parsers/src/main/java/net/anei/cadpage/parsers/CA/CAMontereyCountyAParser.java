package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Monterey County, CA
 */
public class CAMontereyCountyAParser extends FieldProgramParser {

  public CAMontereyCountyAParser() {
    super("MONTEREY COUNTY", "CA",
          "SRC UNIT CALL ADDR! CROSS_STREETS%EMPTY! X EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "donotreply@co.monterey.ca.us,donotreply@co.monterey,MONTEREY911@CO.MONTEREY.CA.US";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    String[] flds = body.split(";;", -1);
    if (flds.length >= 6) {
      return parseFields(flds, data);
    }
    if (parseMsg4(body, data)) return true;
    if (parseMsg3(body, data)) return true;
    if (parseMsg1(subject, body, data)) return true;
    if (parseMsg2(body, data)) return true;
    if (parseMsg5(body, data)) return true;
    return false;
  }

  private boolean parseMsg4(String body, Data data) {
    FParser fp = new FParser(body);
    String src = fp.get(8);
    if (!fp.check("  -  ")) return false;
    String unit = fp.get(8);
    if (!fp.check(" ") || fp.check(" ")) return false;
    String call = fp.get(8);
    if (!fp.check(" ") || fp.check(" ")) return false;
    String addr = fp.get(100);
    if (!fp.check(" ")) return false;
    String cross = fp.get();

    setFieldList("SRC UNIT CALL ADDR APT X");
    data.strSource = src;
    data.strUnit = unit;
    data.strCall = call;
    parseAddress(addr, data);
    data.strCross = cross;
    return true;
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d?[A-Z]\\d\\d?[A-Z]?) +(.*)", Pattern.CASE_INSENSITIVE);

  private boolean parseMsg3(String body, Data data) {
    FParser fp = new FParser(body);
    String unit = fp.get(10);
    if (!fp.check(" Address: ")) return false;
    String addr = fp.get(21);
    if (!fp.check("Apt ")) return false;
    String apt = fp.get(5);
    if (!fp.check("X st ")) return false;
    String cross = fp.get(30);
    if (!fp.check("City ")) return false;
    String city = fp.get(10);
    if (!fp.check(" ")) return false;
    if (fp.check(" ")) return false;
    String call = fp.get();

    setFieldList("UNIT ADDR APT X CITY CODE CALL");
    data.strUnit = unit;
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", apt);
    data.strCross = cross;
    data.strCity = city;
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = call;
    return true;
  }

  private static final Pattern MARKER = Pattern.compile("([A-Z ]+) - +");

  private boolean parseMsg2(String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    setFieldList("SRC UNIT CALL ADDR APT X MAP");

    String source = match.group(1);
    body = body.substring(match.end());

    FParser fp = new FParser(body);
    int pt = fp.checkAhead("CROSS STREETS", 68, 92, 93, 94, 142);
    if (pt >= 0) {

      if (pt == 68) {
        String unit = fp.get(6);

        if (fp.check(" ")) return false;
        String call = fp.get(10);

        if (!fp.check(" ")) return false;
        if (fp.check(" ")) return false;
        String addr = fp.get(50);

        if (!fp.check(" CROSS STREETS")) return false;
        String cross = fp.get();

        data.strSource = source;
        data.strUnit = unit;
        data.strCall = call;
        parseAddress(addr, data);
        data.strCross = cross;
        return true;
      }

      if (pt == 142) {
        setFieldList("SRC CALL UNIT ADDR APT X");
        String call = fp.get(10);
        if (fp.check(" ")) return false;
        String unit = fp.get(30);
        if (!fp.check(" ") || fp.check(" ")) return false;
        String addr = fp.get(100);
        if (!fp.check(" CROSS STREETS")) return false;
        String cross = fp.get(50);

        data.strSource = source;
        data.strCall = call;
        data.strUnit = unit;
        parseAddress(addr, data);
        data.strCross = cross;
        return true;
      }

      int unitLen = pt - 63;
      String unit = fp.get(unitLen);
      if (!fp.check(" ")) return false;

      if (fp.check(" ")) return false;
      String call = fp.get(10);

      if (!fp.check(" ") || fp.check(" ")) return false;
      String addr = fp.get(50);

      if (!fp.check(" CROSS STREETS ")) return false;
      String cross = fp.get();

      data.strSource = source;
      data.strUnit = unit;
      data.strCall = call;
      parseAddress(addr, data);
      data.strCross = cross;
      return true;
    }

    pt = fp.checkAhead("X STREETS", 92, 93);
    if (pt >= 0) {

      String unit = fp.get(29);
      if (!fp.check(" ")) return false;

      if (fp.check(" ")) return false;
      String call = fp.get(10);

      if (!fp.check(" ") || fp.check(" ")) return false;
      String addr = fp.get(50);

      if (!fp.check(" X STREETS ")) return false;
      String cross = fp.get(76);

      fp.setOptional();
      if (!fp.check("MAP PAGE")) return false;
      String map = fp.get();

      data.strSource = source;
      data.strUnit = unit;
      data.strCall = call;
      parseAddress(addr, data);
      data.strCross = cross;
      data.strMap = map;
      return true;

    }

    if (fp.checkAhead(142, " X STRTS  ")) {
      String unit = fp.get(30);
      if (!fp.check(" ") || fp.check(" ")) return false;
      String call = fp.get(10);
      if (!fp.check(" ") || fp.check(" ")) return false;
      String addr = fp.get(100);
      if (!fp.check(" X STRTS  ")) return false;
      String cross = fp.get(60);
      if (!fp.checkBlanks(14)) return false;
      String gps1 = fp.get(10);
      if (!fp.checkBlanks(13)) return false;
      String gps2 = fp.get(10);
      if (!fp.checkBlanks(10)) return false;
      String id =fp.get(12);
      if (!fp.checkBlanks(10)) return false;
      String zip = fp.get(5);
      if (!fp.checkBlanks(6)) return false;
      String city = fp.get();
      if (city.isEmpty()) city = zip;

      setFieldList("SRC UNIT CALL ADDR APT X GPS ID CITY");
      data.strSource = source;
      data.strUnit = unit;
      data.strCall = call;
      parseAddress(addr, data);
      data.strCross = cross;
      setGPSLoc(cvtGps(gps1)+','+cvtGps(gps2), data);
      data.strCallId = id;
      data.strCity = city;
      return true;
    }

    if (fp.checkAhead(243, "MAP PAGE")) {
      String unit = fp.get(20);
      if (!fp.check(" ") || fp.check(" ")) return false;
      String call = fp.get(9);
      if (fp.check(" ")) return false;
      String addr = fp.get(50);

      data.strUnit = unit;
      data.strCall = call;
      parseAddress(addr, data);
      return true;
    }

    String call, unit;
    if (fp.checkAhead(30, " ") && !fp.checkAhead(31,  " ")) {
      unit = fp.get(30);
      if (!fp.check(" ") || fp.check(" ")) return false;
      call = fp.get(10);
      if (!fp.check(" ") || fp.check(" ")) return false;
    } else {
      call = fp.get(10);
      if (fp.check(" ")) return false;
      unit = fp.get(30);
      if (!fp.check(" ") || fp.check(" ")) return false;
    }
    String addr = fp.get(40);
    if (!fp.checkBlanks(361)) return false;
    String cross = fp.get();

    data.strSource = source;
    data.strUnit = unit;
    data.strCall = call;
    parseAddress(addr, data);
    data.strCross = cross;
    return true;
  }

  private String cvtGps(String gps) {
    int pt = gps.length()-6;
    if (pt >= 0) gps = gps.substring(0,pt)+'.'+gps.substring(pt);
    return gps;
  }

  private static final Pattern MASTER = Pattern.compile("(?:(.*?) - )?([A-Z0-9]{2,6}):(.*?) - (.*?)(?: - ([A-Z]{3}))? *(?:Units?:(.*?))?");

  private boolean parseMsg1(String subject, String body, Data data) {
    // Old page format

    if (!subject.equals("CAD Page")) return false;
    setFieldList("MAP CODE CALL ADDR PLACE CITY UNIT INFO");

    int pt = body.indexOf('\n');
    String extra = null;
    if (pt >= 0) {
      extra = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    data.strMap = getOptGroup(match.group(1));
    data.strCode = match.group(2);
    data.strCall = match.group(3).trim();
    parseAddress(match.group(4).trim(), data);
    String city = match.group(5);
    if (city != null) data.strCity = convertCodes(city, CITY_CODES);
    data.strUnit = getOptGroup(match.group(6));

    pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strPlace = data.strCity.substring(0,pt);
      data.strCity = data.strCity.substring(pt+1);
    }

    if (extra != null) {
      if (extra.startsWith("Message:")) extra = extra.substring(8).trim();
      data.strSupp = extra;
    }

    return true;
  }

  private boolean parseMsg5(String body, Data data) {
    FParser fp = new FParser(body);
    String src = fp.get(9);
    if (!src.endsWith(" FIRE") && !src.endsWith(" EMS")) return false;
    if (!fp.check(" ")) return false;
    if (fp.check(" ")) return false;
    String unit = fp.get(30);
    if (!fp.check(" ")) return false;
    if (fp.check(" ")) return false;
    String call = fp.get(30);
    if (!fp.check(" ")) return false;
    if (fp.check(" ")) return false;
    String addr = fp.get(100);
    if (!fp.check(" ")) return false;
    String cross = fp.get();

    setFieldList("SRC UNIT CALL ADDR APT X");
    data.strSource = src;
    data.strUnit = unit;
    data.strCall = call;
    parseAddress(addr, data);
    data.strCross = cross;
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "LAS PALMAS RANCH",     "SALINAS"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARO",   "AROMAS",
      "ARS",   "ARROYO SECO/GREENFIELD",
      "BKN",   "BOLSA KNOLLS",
      "BOR",   "BORONDA",
      "BRD",   "BRADLEY",
      "CAS",   "CASTROVILLE",
      "CDT",   "CORRAL DE TIERRA",
      "CHI",   "CARMEL HIGHLANDS",
      "CHS",   "CARMEL HILLS",
      "CHU",   "CHUALAR",
      "CML",   "CARMEL",
      "CMM",   "CARMEL MEADOWS/CARMEL",
      "CMP",   "CARMEL POINT",
      "CMV",   "CARMEL VALLEY",
      "CMW",   "CARMEL WOODS",
      "CST",   "COAST/CARMEL",
      "CSU",   "CAL STATE UNIVERSITY/SEASIDE",
      "CVR",   "CARMEL VALLEY RANCH/CARMEL",
      "CVW",   "CARMEL VIEWS/CARMEL",
      "DLI",   "DEFENSE LANGUAGE INSTITUTE/MONTEREY",
      "DMF",   "DEL MONTE FOREST",
      "DRO",   "DEL REY OAKS",
      "FTO",   "FORT ORD/SEASIDE",
      "GON",   "GONZALES",
      "GRN",   "GREENFIELD",
      "HHS",   "HIDDEN HILLS/SALINAS",
      "HIM",   "HIGH MEADOWS",
      "HTF",   "HATTON FIELDS",
      "IND",   "INDIAN SPRINGS/SALINAS",
      "JOL",   "JOLON",
      "KCY",   "KING CITY",
      "LAM",   "LAMESA VILLAGE/MONTEREY",
      "LLG",   "LOS LAURELES GRADE/SALINAS",
      "LLS",   "LAS LOMAS",
      "LOC",   "LOCKWOOD/MONTEREY",
      "LPR",   "LAS PALMAS RANCH/SALINAS",
      "LSE",   "LAGUNA SECA ESTATES/SALINAS",
      "MAR",   "MARINA",
      "MCO",   "",                            // MONTEREY COUNTY
      "MDL",   "MONTE DEL LAGO/CASTROVILLE",
      "MPA",   "MONTEREY AIRPORT/MONTEREY",
      "MSF",   "MISSION FIELDS",
      "MSL",   "MOSS LANDING",
      "MTR",   "MONTERA RANCH/MONTEREY",
      "MTY",   "MONTEREY",
      "NAF",   "NAVY ANNEX FACILITY/MONTEREY",
      "NPS",   "NAVY POSTGRADUATE SCHOOL/MONTEREY",
      "OHS",   "OAK HILLS",
      "ORD",   "FT ORD/SEASIDE",
      "PAC",   "PACIFIC GROVE",
      "PAJ",   "PAJARO",
      "PGS",   "POSTGRADUATE SCHOOL/MONTEREY",
      "PRU",   "PRUNEDALE",
      "RDR",   "ROBLES DEL RIO",
      "SAR",   "SAN ARDO",
      "SBC",   "SAN BENITO COUNTY",
      "SBN",   "SAN BENANCIO",
      "SCO",   "",                          // SOUTH COUNTY
      "SCY",   "SAND CITY",
      "SEA",   "SEASIDE",
      "SLP",   "SANTA LUCIA PRESERVE/CARMEL",
      "SLU",   "SAN LUCAS",
      "SNS",   "SALINAS",
      "SOL",   "SOLEDAD",
      "SPK",   "SPRECKELS",
      "SRV",   "SERRA VILLAGE/SALINAS",
      "TPE",   "TORO PARK ESTATES/SALINAS",
      "VGR",   "VALLEY GREENS/CARMEL",
      "WAT",   "WATSONVILLE",
      "YKP",   "YANKEE POINT",

  });
}
