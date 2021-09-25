package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class TXDentonCountyAParser extends DispatchOSSIParser {

  private static final Pattern SUBJECT_PTN = Pattern.compile("FYI|Update|Cancel|\\d{9};\\d\\d/\\d\\d/\\d{4} \\d\\d");

  public TXDentonCountyAParser() {
    super(CITY_LIST, "DENTON COUNTY", "TX",
          "ID?: ( CANCEL ADDR ( SHORT_CITY | PLACE! ) " +
               "| FYI? DATIME? ID DATIME? ( SRC CALL ( ADDR/Z CITY | PLACE? ADDR X/Z+? CITY ) " +
                                         "| CALL ( PLACE ADDR/Z CITY | ADDR/Z CITY | PLACE ADDR | ADDR ) ( SRC | X/Z SRC | X/Z X/Z SRC | X+? ) UNIT? " +
                                         ") "  +
               ") " +
          "INFO/N+? GPS1 GPS2 INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@dentoncounty.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0 && body.startsWith("CAD:")) {
      String extra = null;
      if (SUBJECT_PTN.matcher(subject).matches()) {
        extra = "CAD:" + subject + ":";
        if (!body.startsWith(extra)) body = extra + body.substring(4);
      }
    }
    else if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL|.* ENROUTE|MUTUAL AID|CPR IN PROGRESS|DEATH ON SCENE", true);
    if (name.equals("SHORT_CITY")) return new MyShortCityField();
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("DATIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,2}[FP]D|B620|CNTY", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+(?:,[A-Z]+\\d+)*", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyShortCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
        return true;
      }
      if (isCity(field)) {
        data.strCity = field;
        return true;
      }
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("FM\\d+");
  private static final Pattern ADDR_SERV_PTN = Pattern.compile("\\bSERV\\b");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("-(?:RM|[RASLU])([A-Z]|\\d+[A-Z]?)\\b");
  private class MyAddressField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(field, data, false);
    }

    @Override
    public void parse(String field, Data data) {
      checkParse(field, data, true);
    }

    private boolean checkParse(String field, Data data, boolean force) {

      if (!force && ADDR_UNIT_PTN.matcher(field).matches()) return false;

      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }

      field = ADDR_SERV_PTN.matcher(field).replaceAll("FRONTAGE");

      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.find()) {
        apt = match.group(1);
        field = field.substring(0,match.start()) + field.substring(match.end());
      }

      if (!force) {
        if (!super.checkParse(field, data)) return false;
      } else {
        super.parse(field, data);
      }

      data.strApt = append(apt, "-", data.strApt);
      return true;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY?";
    }
  }

  // Throw an error if  cross fields contain more than 2 streets
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (ADDR_UNIT_PTN.matcher(field).matches()) return false;
      if (data.strCross.contains("&")) return false;
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strCross.contains("&")) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{4,}");
  private static final Pattern TRUN_GPS_PTN = Pattern.compile("[-+]?\\d[.\\d]*");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Pattern ptn = isLastField() ? TRUN_GPS_PTN : GPS_PTN;
      if (!ptn.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (! checkParse(field, data)) abort();
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(15).trim();
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARGY", "ARGYLE",
      "AUBR", "AUBREY",
      "BART", "BARTONVILLE",
      "CORI", "CORINTH",
      "CROS", "CROSS ROADS",
      "DC",   "DENTON COUNTY",
      "DENT", "DENTON",
      "DOUB", "DOUBLE OAK",
      "FRIS", "FRISCO",
      "HICK", "HICKORY CREEK",
      "JUST", "JUSTIN",
      "LAKE", "LAKE DALLAS",
      "LITT", "LITTLE ELM",
      "NORT", "NORTH LAKE",
      "PILO", "PILOT POINT",
      "PROV", "PROVIDENCE VILLAGE",
      "ROAN", "ROANOKE",
      "SANG", "SANGER",
      "TROP", "TROPHY CLUB",
      "WIS",  "WISE COUNTY"
  });

  private static final String[] CITY_LIST = new String[]{
      "ARGYLE",
      "AUBREY",
      "BARTONVILLE",
      "BOLIVAR",
      "CARROLLTON",
      "COPPELL",
      "COPPER CANYON",
      "CORINTH",
      "CORRAL CITY",
      "CROSSROADS",
      "CROSS ROADS",
      "DALLAS",
      "DENTON",
      "DISH",
      "DOUBLE OAK",
      "ELIZABETHTOWN",
      "FLOWER MOUND",
      "FORT WORTH",
      "FRISCO",
      "HACKBERRY",
      "HASLET",
      "HEBRON",
      "HICKORY CREEK",
      "HIGHLAND VILLAGE",
      "JUSTIN",
      "KRUGERVILLE",
      "KRUM",
      "LAKE DALLAS",
      "LAKEWOOD VILLAGE",
      "LEWISVILLE",
      "LINCOLN PARK",
      "LITTLE ELM",
      "NORTHLAKE",
      "OAK POINT",
      "PILOT POINT",
      "PLANO",
      "PONDER",
      "PROSPER",
      "PROVIDENCE VILLAGE",
      "ROANOKE",
      "SANGER",
      "SHADY SHORES",
      "SLIDELL",
      "SOUTHLAKE",
      "THE COLONY",
      "TROPHY CLUB",
      "WESTLAKE",

      "COOKE COUNTY",
      "COLLIN COUNTY",
      "DALLAS COUNTY",
      "DENTON COUNTY",
      "GRAYSON COUNTY",
      "TARRANT COUNTY",
      "WISE COUNTY"
  };
}
