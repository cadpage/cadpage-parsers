package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Augusta County, VA
 */
public class VAAugustaCountyParser extends DispatchOSSIParser {

  private static final Pattern DELIM_PTN = Pattern.compile("(?<!\\b(?:CAD|DIST|FYI|Update)):");


  public VAAugustaCountyParser() {
    this("AUGUSTA COUNTY", "VA");
  }

  public VAAugustaCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
           "FYI? CALL! ( ADDR/SZ! END | ( PLACE ID ADDR/S! | PLACE? ADDR/S! ) MAP? CITY? INFO+ )");
    setupCities(CITY_CODES);
    removeWords("MALL");
  }

  @Override
  public String getFilter() {
    return "cad@co.augusta.va.us,777";
  }

  @Override
  public String getAliasCode() {
    return "VAAugustaCounty";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // This may bite us someday, but it is a convenient way to identify
    // VAWaynesboroB calls
    if (subject.length() > 0 &&
        !subject.equals("Text Message") &&
        !subject.equals("%Text Message%") &&
        !subject.equals("%Augusta 911%")) return false;
    if (body.startsWith("CAD:CANCEL;")) return false;

    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (body.startsWith("CAD:%")) {
      pt = body.indexOf('%', 5);
      if (pt < 0) return false;
      body = "CAD:" + body.substring(pt+1);
    }
    body = DELIM_PTN.matcher(body).replaceAll(";");
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), CITY_CODES);
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{5,}");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {

      // Anything starting with a digit is assumed to be an address
      if (field.length() == 0) return false;
      if (Character.isDigit(field.charAt(0))) {
        parse(field, data);
        return true;
      }

      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("- ");
      if (pt >= 0) {
        String city = field.substring(pt+2).trim();
        field = field.substring(0,pt).trim();
        if (isCity(city)) {
          data.strCity = city;
        } else {
          data.strPlace = append(data.strPlace, " - ", city);
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  // Map field recognizes and isolates a trailing map pattern
  private static final Pattern MAP_PTN = Pattern.compile("\\b\\d{3}-?\\d{2}$");
  private class MyMapField extends MapField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.find()) return false;
      field = match.group();
      if (field.indexOf('-') < 0) {
        field = field.substring(0,3) + '-' + field.substring(3);
      }
      parse(field, data);
      return true;
    }
  }

  // Info field contains all kinds of sloppy stuff
  private static final Pattern INFO_ID_PTN = Pattern.compile("\\d{5,}");
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:ROOM|RM|APT) *(.*)");
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("CNTY-.*|MED-\\d|HRECC?|SEOC|WEOC");
  private static final Pattern INFO_MAP_PTN = Pattern.compile("\\d{2,3}-\\d{2}");
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {

      if (data.strCallId.isEmpty() && INFO_ID_PTN.matcher(field).matches()) {
        data.strCallId = field;
        return;
      }

      // Info field are frequently broken up by slashes :(
      for (String fld : field.split("/")) {
        fld = fld.trim();

        if (field.equals("Radio Channel")) continue;

        if (fld.equals(data.strAddress)) continue;

        String tmp = fld.toUpperCase();
        if (tmp.startsWith("PO ") || tmp.startsWith("P O ")) continue;
        Matcher match = INFO_APT_PTN.matcher(fld);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          continue;
        }

        if (INFO_MAP_PTN.matcher(fld).matches()) {
          data.strMap = fld;
          continue;
        }

        if (INFO_CHANNEL_PTN.matcher(fld).matches()) {
          if (data.strChannel.isEmpty()) data.strChannel = fld;
          continue;
        }

        String city = field;
        int pt = city.indexOf('(');
        if (pt >= 0) city = city.substring(0,pt).trim();
        if (isCity(city)) {
          data.strCity = city;
          continue;
        }

        int addrStat = checkAddress(fld);
        if (addrStat > STATUS_MARGINAL) {
          // If this is better than a naked road, see if
          // we previously misidentified a place name as an
          // address
          if (addrStat > STATUS_STREET_NAME && data.strPlace.length() == 0 && checkAddress(data.strAddress) == STATUS_STREET_NAME) {
            data.strPlace = data.strAddress;
            data.strAddress = "";
            parseAddress(fld, data);
            continue;
          }
          data.strCross = append(data.strCross, " & ", fld);
          continue;
        }

        data.strSupp = append(data.strSupp, "\n", fld);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT MAP PLACE X ID INFO CH";
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "STAUNTON",
      "WAYNESBORO",

      // Towns
      "CRAIGSVILLE",
      "GRO",
      "GROTTOES",

      // Census-designated places
      "AUGUSTA SPRINGS",
      "CHURCHVILLE",
      "CRIMORA",
      "DEERFIELD",
      "DOOMS",
      "GREENVILLE",
      "HARRISTON",
      "FISHERSVILLE",
      "JOLIVUE",
      "LYNDHURST",
      "MIDDLEBROOK",
      "MOUNT SIDNEY",
      "NEW HOPE",
      "SHERANDO",
      "STUARTS DRAFT",
      "VERONA",
      "WEYERS CAVE",
      "WINTERGREEN",

      // Other unincorporated communities
      "FORT DEFIANCE",
      "LOVE",
      "MOUNT SOLON",
      "SPOTTSWOOD",
      "SWOOPE",
      "WEST AUGUSTA",

      // Albemarle County
      "ALBEMARLE",
      "ALBEMARLE COUNTY",
      "AFTON",

      // Bath County
      "BATH",
      "BATH COUNTY",

      // Highland County
      "HIGH",
      "HIGHLAND",
      "HIGHLAND COUNTY",

      // Rockbridge County
      "ROCKBRIDGE",
      "ROCKBRIDGE COUNTY",
      "GOSHEN",
      "RAPHINE",

      //  Rockingham County
      "ROCKINGHAM",
      "ROCKINGHAM COUNTY",
      "BRIDGEWATER",
      "MT CRAWFORD"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AFT",  "AFTON",
      "AUG",  "AUGUST SPRINGS",
      "BRAO", "BROADWAY",
      "BWT",  "BRIDGEWATER",
      "CHU",  "CHURCHVILLE",
      "CRI",  "CRIMORA",
      "DAYT", "DAYTON",
      "DEE",  "DEERFIELD",
      "FAIR", "FAIRFIELD",
      "FISH", "FISHERSVILLE",
      "FTD",  "FORT DEFIANCE",
      "GOR",  "GROTTOES",
      "GOS",  "GOSHEN",
      "GRE",  "GREENVILLE",
      "GRG",  "CRAIGSVILLE",
      "GRO",  "GROTTOES",
      "HARR", "HARRISONBURG",
      "HIGH", "HIGHLAND COUNTY",
      "KES",  "KESWICK",
      "LEXI", "LEXINGTON",
      "LYN",  "LYNDHURST",
      "MID",  "MIDDLEBROOK",
      "MIDL", "MIDLOTHIAN",
      "MS",   "MINT SPRINGS",
      "MSN",  "MOUNT SOLON",
      "MTC",  "MT CRAWFORD",
      "MTS",  "MOUNT SIDNEY",
      "NELL", "NELLYSFORD",
      "NH",   "NEW HOPE",
      "RAP",  "RAPHINE",
      "SHE",  "SHENANDOAH",
      "SPO",  "SPOTTSWOOD",
      "STD",  "STUARTS DRAFT",
      "STE",  "STEELES TAVERN",
      "STN",  "STAUNTON",
      "SWO",  "SWOOPE",
      "VAB",  "VIRGINIA BEACH",
      "VER",  "VERONA",
      "VESU", "VESUVIUS",
      "WAG",  "WEST AUGUSTA",
      "WC",   "WEYERS CAVE",
      "WYN",  "WAYNESBORO"

  });

}
