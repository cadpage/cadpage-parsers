package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Lacombe County, AB, CA
 */
public class DispatchA51Parser extends FieldProgramParser {

  protected DispatchA51Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  protected DispatchA51Parser(String[] cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "( Sent:DATETIME3 INFO/G! INFO/N+ " +
          "| SELECT/2 CALL CALL2? LOCATION ADDR VILLAGE_OF? CITY/Z? ( APT UNITS_RESPONDING! | UNITS_RESPONDING! ) UNIT+ " +
          "| DATETIME CALL ADDRCITY INFO/N+ " +
          "| ID:ID? Date:DATETIME! Type:CALL! Severity:PRI? Location:ADDRCITY! " +
                "( Business_Name:PLACE! Subdivision:PLACE/SDS! Common_Place:PLACE/SDS! GPS0? " +
                "| ( Location_Description:PLACE_MAP | Description:PLACE_MAP ) Units_Selected:UNIT PrePlan_Number:LINFO/N Units:UNIT Latitude:GPS1 Longitude:GPS2 TAC_Channel:CH Units:UNIT " +
                ") Units_Responding:UNIT Notes:INFO/N+ " +
          ")");
  }

  private static final Pattern DELIM = Pattern.compile(";?\n");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("LatitudeUnits Responding", "Units Responding");
    body = body.replace('\\', '/');
    if (body.startsWith("/ ")) {
      setSelectValue("2");
      body = body.replace("\n", " / ");
      if (!parseFields(body.substring(2).trim().split(" / "), data)) return false;
    } else {
      setSelectValue("1");
      body = stripFieldEnd(body, ";");
      if (!parseFields(DELIM.split(body), data)) return false;
      setGPSLoc(data.strGPSLoc, data);
      if (data.strAddress.startsWith("Unknown Location -") && data.strGPSLoc.length() > 0) {
        data.strAddress = data.strGPSLoc;
      }
    }
    return true;
  }

  private static final DateFormat DATE_TIME_FMT3 = new SimpleDateFormat("yyyy/MMM/dd HH:mm");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME3")) return new DateTimeField(DATE_TIME_FMT3, true);
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("CALL2")) return new CallField("Gas Odor.*");
    if (name.equals("LOCATION")) return new SkipField("Location", true);
    if (name.equals("VILLAGE_OF")) return new SkipField("VILLAGE OF");
    if (name.equals("APT")) return new AptField("Unit +(.*)");
    if (name.equals("UNITS_RESPONDING")) return new SkipField("Units Responding", true);
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("PLACE_MAP")) return new BasePlaceMapField();
    if (name.equals("GPS0")) return new GPSField("\\(([-+]?\\d+\\.\\d+, *[-+]?\\d+\\.\\d+)\\)", true);
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\w+) - +(.*)");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final DateFormat DATE_TIME_FMT1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final Pattern EXT_DATE_TIME_PTN = Pattern.compile("(.*?)T(.*?)-.*");

  private class BaseDateTimeField extends DateTimeField {

    public BaseDateTimeField() {
      super(DATE_TIME_FMT1, true);
    }
    @Override
    public void parse(String field, Data data) {
      Matcher match = EXT_DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1) + ' ' + match.group(2);
      }
      super.parse(field, data);
    }
  }

  private static final Pattern TRAIL_SEMI_PTN = Pattern.compile("(.*?)[; ]+");
  private static Pattern STATE_CODE_PTN = Pattern.compile("(.*?)[, ]+(AB|BC)");
  private static Pattern APT_PTN = Pattern.compile("(?:Unit |#) *([^, ]+)[- ,]*(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = TRAIL_SEMI_PTN.matcher(field);
      if (match.matches()) field = match.group(1);

      if (field.endsWith(")")) {
        if (field.startsWith("LL(")) {
          data.strAddress = field;
          return;
        }
        int pt = field.indexOf('(');
        if (pt >= 0) {
          data.strPlace = field.substring(pt+1,field.length()-1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      String apt = "";
      match = STATE_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strState = match.group(2);
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.startsWith("Unit ")) {
        data.strApt = city.substring(5).trim();
        city = p.getLastOptional(',');
      }
      data.strCity = city;

      field = p.get();
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(match.group(1), "-", data.strApt);
        field = match.group(2);
      }
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt);
      field = field.replace('\\', '/');
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      } else {
        parseAddress(field, data);
      }
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
  }

  private class BasePlaceMapField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("Map:");
      if (pt >= 0) {
        data.strMap = field.substring(pt+4).trim();
        field = field.substring(0,pt).trim();
      }
      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "PLACE MAP";
    }

  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("LONG/LAT: Long=(\\S+) +Lat=(\\S+)");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strGPSLoc.isEmpty()) {
        Matcher match = INFO_GPS_PTN.matcher(field);
        if (match.matches()) {
          setGPSLoc(match.group(2)+','+match.group(1), data);
          return;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS?";
    }
  }
}
