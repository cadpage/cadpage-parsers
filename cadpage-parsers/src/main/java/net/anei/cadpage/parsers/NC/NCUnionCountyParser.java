package net.anei.cadpage.parsers.NC;

/**
 * Union County, NC
 */
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCUnionCountyParser extends DispatchOSSIParser {

  public NCUnionCountyParser() {
    super(CITY_LIST, "UNION COUNTY", "NC",
           "( CANCEL ADDR CITY2 PLACE2 " +
           "| FYI? ID? ADDR ( CITY ID? | CITY/Z ID | ID? ) CALL! SRC? CH? INFO1/N+? DATETIME ID? UNIT Radio_Channel:CH INFO/N+? GPS1 GPS2 " +
           ") INFO/N+");
    setupSaintNames("JOHNS", "SIMONS");
    setupProtectedNames("BURGESS AND HELMS", "SUGAR AND WINE");
    setupMultiWordStreets("INDIAN TRAIL FAIRVIEW");
  }

  @Override
  public String getFilter() {
    return "cad@uc.co.union.nc.us,cad@webmail.co.union.nc.us,cad@co.union.nc.us,CAD@unioncountync.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    boolean good = body.startsWith("CAD:");
    if (!good) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    return good || data.strCity.length() > 0 || data.strCallId.length() > 0;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("UNIT_CALL")) return new MyUnitCallField();
    if (name.equals("CITY2")) return new MyCity2Field();
    if (name.equals("PLACE2")) return new MyPlace2Field();
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{2,4}", true);
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("CUSTOM")) return new CustomField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d");
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCancelField extends BaseCancelField {
    public MyCancelField() {
      super("CPR IN PROGRESS|RETONE COMPLETE|STAGING RECOMMENDED");
    }
  }

  private static final Pattern UNIT_CALL_PTN = Pattern.compile("\\{([A-Z0-9 ]+)\\} *(.*)");
  private class MyUnitCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).trim();
      data.strCall = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

  private class MyCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      for (String part :  PLACE_DIR_PTN.split(field)) {
        if (data.strCity.isEmpty()) {
          data.strCity = convertCodes(part, CITY_CODES);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY PLACE";
    }
  }

  private Pattern PLACE_DIR_PTN = Pattern.compile(" *\\([NSEW]\\) *");
  private class MyPlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = PLACE_DIR_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

  // Custom field is an optional 6 character field that
  // different departments use for different purposes.  We will look at the
  // previous source field to figure out which department this is and what
  // they want us to do with it.

  private static final Pattern SRC_CHANNEL_PTN = Pattern.compile("S\\d\\d|CH *\\d");
  private class CustomField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() > 6 || field.contains(" ")  && !field.startsWith("CH ")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (SRC_CHANNEL_PTN.matcher(data.strSource).matches()) {
        data.strChannel = field;
      } else {
        data.strUnit = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CH UNIT?";
    }
  }

  private class MyChannelField extends ChannelField {

    MyChannelField() {
      super("[A-M][-A-Z0-9]{1,5}|.* OPS .*", false);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strChannel.length() > 0) return;
      super.parse(field, data);
    }

  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (isValidAddress(field)) {
        data.strCross = append(data.strCross, " & ", field);
      } else if (data.strPlace.length() == 0) {
        data.strPlace = field;
      } else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X INFO?";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{5,}");
  private static final Pattern TRUNC_GPS1_PTN = Pattern.compile("\\+3[45].?\\d*");
  private class MyGPSField extends GPSField {

    private final int type;

    public MyGPSField(int type) {
      super(type);
      this.type = type;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(2)) return false;
      if (!GPS_PTN.matcher(field).matches()) {
        if (type == 2) return true;
        return (isLastField(1) && TRUNC_GPS1_PTN.matcher(field).matches());
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "FAIRVIEW",
    "HEMBY BRIDGE",
    "INDIAN TRAIL",
    "LAKE PARK",
    "MARSHVILLE",
    "MARVIN",
    "MINERAL SPRINGS",
    "MONROE",
    "STALLINGS",
    "UNIONVILLE",
    "WAXHAW",
    "WEDDINGTON",
    "WESLEY CHAPEL",
    "WINGATE",

    // Anson County
    "PEACHLAND",

    // Mecklenburg County
    "MATTHEWS"
  };

  // CIty codes are only used for cance/under control/confirmed fire updates
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FAI", "FAIRVILLE",
      "HEM", "HEMBY BRIDGE",
      "IND", "INDIAN TRAIL",
      "LAK", "LAKE PARK",
      "MAR", "MARVIN",
      "MAT", "MATTHEWS",
      "MIN", "MINERAL SPRINTS",
      "MON", "MONROE",
      "MSH", "MARSHVILLE",
      "OAK", "OAKBORO",
      "PEA", "PEACHLAND",
      "STA", "STALLINGS",
      "UNI", "UNIONVILLE",
      "WAX", "WAXHAW",
      "WED", "WEDDINGTON",
      "WES", "WESLEY CHAPEL",
      "WES1","WESLEY CHAPEL",
      "WIN", "WINGATE"
  });
}
