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
          "| ID:ID? Date:DATETIME! Type:CALL! Location:ADDRCITY! Location_Description:PLACE Units_Selected:UNIT PrePlan_Number:LINFO/N Units:UNIT Latitude:GPS1 Longitude:GPS2 Units:UNIT Units_Responding:UNIT Notes:INFO/N+ " +
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
  private static final DateFormat DATE_TIME_FMT1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME3")) return new DateTimeField(DATE_TIME_FMT3, true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new CallField("Gas Odor.*");
    if (name.equals("LOCATION")) return new SkipField("Location", true);
    if (name.equals("VILLAGE_OF")) return new SkipField("VILLAGE OF");
    if (name.equals("APT")) return new AptField("Unit +(.*)");
    if (name.equals("UNITS_RESPONDING")) return new SkipField("Units Responding", true);
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT1, true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\w+) - +(.*)");
  private class MyCallField extends CallField {
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

  private static final Pattern TRAIL_SEMI_PTN = Pattern.compile("(.*?)[; ]+");
  private static Pattern STATE_CODE_PTN = Pattern.compile("(.*?)[, ]+(AB|BC)");
  private static Pattern APT_PTN = Pattern.compile("(?:Unit |#) *([^, ]+)[- ,]*(.*)");
  private class MyAddressCityField extends AddressCityField {
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

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
}
