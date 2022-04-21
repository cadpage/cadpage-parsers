package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class NCJonesCountyParser extends DispatchA3Parser {

  public NCJonesCountyParser() {
    this("JONES COUNTY", "NC");
  }

  public NCJonesCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "MASH! Line16:INFO! Line17:INFO! Line18:INFO!");
    addExtendedDirections();
    setBreakChar('=');
    setupSpecialStreets("LOWER ST");
    setupProtectedNames("DR GEO L EDWARDS");
  }

  @Override
  public String getAliasCode() {
    return "NCJonesCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Communication:");
    return super.parseMsg(body, data, false);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    return super.getField(name);
  }

  private static final Pattern MASTER = Pattern.compile("(.*?)(?: +(\\d{5}))?(?: +Geo Comment:(.*?))? +([EFL]-[^ ]+|TRAFFIC STOP)\\b(.*?)(?: +([,A-Z0-9]*(?:[A-Z]+\\d+|FD|[^L]EMS|NCHP)))?");
  private static final Pattern LEAD_DOTS = Pattern.compile("^[ \\.]+");
  private static final Pattern ADDR_APTS = Pattern.compile("(.* APTS) +([^ ]+)");
  private static final Pattern CLEAN_CROSS = Pattern.compile("[ /]*(.*?)[ /]*");

  private class MyMashField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = MASTER.matcher(field);
      if (!mat.matches()) abort();
      String addr = mat.group(1).trim();
      String zip = getOptGroup(mat.group(2));
      String place = getOptGroup(mat.group(3));
      data.strCode = mat.group(4).trim();
      String callCode = mat.group(5).trim();
      data.strUnit = getOptGroup(mat.group(6));

      int pt = callCode.indexOf('-');
      if (pt >= 0) {
        data.strCode = append(data.strCode, " ", callCode.substring(0,pt).trim());
        callCode = callCode.substring(pt+1).trim();
      }
      data.strCall = callCode;

      data.strPlace = LEAD_DOTS.matcher(place).replaceFirst("");

      addr = addr.replace("//", "&").replace(" N/S ", " ").replace(" E/W ", " ");
      parseAddress(StartType.START_ADDR, FLAG_PREF_TRAILING_DIR | FLAG_RECHECK_APT| FLAG_CROSS_FOLLOWS, addr, data);
      mat = ADDR_APTS.matcher(data.strAddress);
      if (mat.matches()) {
        data.strAddress = mat.group(1);
        data.strApt = append(mat.group(2), "-", data.strApt);
      }

      data.strAddress = stripFieldEnd(data.strAddress, "`");
      data.strApt = stripFieldEnd(data.strApt, "`");

      // if no city has been selected yet, check whatsLeft for the city and extract it.
      String whatsLeft = getLeft();
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, whatsLeft, data);
        whatsLeft = getLeft();
      }

      // parse and clean crossroads
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_PREF_TRAILING_DIR | FLAG_ANCHOR_END, whatsLeft, data);
      if (data.strCross.length() > 0) {
        Matcher crossMat = CLEAN_CROSS.matcher(data.strCross);
        if (crossMat.matches()) data.strCross = crossMat.group(1);
      }

      if (data.strCity.equalsIgnoreCase("POLLOCKSVIL")) data.strCity = "POLLOCKSVILLE";
      if (data.strCity.length() == 0) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X PLACE CODE CALL UNIT";
    }
  }

  public static String[] CITY_LIST = new String[] {

    // Jones County
    "MAYSVILLE",
    "POLLOCKSVIL",   // Misspelled
    "POLLOCKSVILLE",
    "TRENTON",

    "COMFORT",
    "OAK GROVE",

    "WHITE OAK TWP",
    "POLLOCKSVILLE TWP",
    "TRENTON TWP",
    "CYPRESS CREEK TWP",
    "TUCKAHOE TWP",
    "CHINQUAPIN TWP",
    "BEAVER CREEK TWP",

    // Lenoir County
    "ALBERTSON",
    "KINSTON",
    "GRIFTON",
    "LA GRANGE",
    "LAGRANGE",
    "PINK HILL",

    "DEEP RUN",
    "LIDDELL",
    "TICK BITE",

    "CONTENTNEA NECK TWP",
    "FALLING CREEK TWP",
    "INSTITUTE TWP",
    "KINSTON TWP",
    "MOSELEY HALL TWP",
    "NEUSE TWP",
    "PINK HILL TWP",
    "SAND HILL TWP",
    "SOUTHWEST TWP",
    "TRENT TWP",
    "VANCE TWP",
    "WOODINGTON TWP",

    // Craven County
    "COVE CITY",
    "DOVER",
    "NEW BERN",

    // Duplin County
    "BEULAVILLE",

    // Greene County
    "HOOKERTON",
    "SNOW HILL",

    // Onslow County
    "ONSLOW",
    "RICHLANDS",

    // Wayne County
    "BROGDEN",
    "DUDLEY",
    "ELROY",
    "EUREKA",
    "FREMONT",
    "GOLDSBORO",
    "GRANTHAM",
    "MARMAR",
    "MOUNT OLIVE",
    "PIKEVILLE",
    "ROSEWOOD",
    "SEVEN SPRINGS",
    "WALNUT CREEK"
  };
}
