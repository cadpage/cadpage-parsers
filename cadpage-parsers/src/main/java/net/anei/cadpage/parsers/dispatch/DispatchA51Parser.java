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
  
  private String version;
  
  protected DispatchA51Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/2 CALL CALL2? LOCATION ADDR VILLAGE_OF? CITY/Z? ( APT UNITS_RESPONDING! | UNITS_RESPONDING! ) UNIT+ | " +
            "ID:ID? Date:DATETIME! Type:CALL! Location:ADDRCITY! Units:UNIT? Latitude:GPS1? Longitude:GPS2? Units:UNIT? Units_Responding:UNIT Notes:INFO/N+ )");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replace("LatitudeUnits Responding", "Units Responding");
    body = body.replace('\\', '/');
    if (body.startsWith("/ ")) {
      version = "2";
      body = body.replace("\n", " / ");
      if (!parseFields(body.substring(2).trim().split(" / "), data)) return false;
    } else {  
      version = "1";
      if (!parseFields(body.split("\n"), data)) return false;
      setGPSLoc(data.strGPSLoc, data);
      if (data.strAddress.startsWith("Unknown Location -") && data.strGPSLoc.length() > 0) {
        data.strAddress = data.strGPSLoc;
      }
    }
    return true;
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("Gas Odor.*");
    if (name.equals("LOCATION")) return new SkipField("Location", true);
    if (name.equals("VILLAGE_OF")) return new SkipField("VILLAGE OF");
    if (name.equals("APT")) return new AptField("Unit +(.*)");
    if (name.equals("UNITS_RESPONDING")) return new SkipField("Units Responding", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      setDateTime(DATE_TIME_FMT, field, data);
    }
  }
  
  private static Pattern STATE_CODE_PTN = Pattern.compile("[A-Z]{2}");
  private static Pattern APT_PTN = Pattern.compile("(?:Unit |#) *([^, ]+)[- ,]*(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
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
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.startsWith("Unit ")) {
        data.strApt = city.substring(5).trim();
        city = p.getLastOptional(',');
      }
      if (STATE_CODE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      
      field = p.get();
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(match.group(1), "-", data.strApt);
        field = match.group(2);
      }
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt);
      parseAddress(field.replace('\\', '/'), data);
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
