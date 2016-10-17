package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CANapaParser extends FieldProgramParser {

  public CANapaParser() {
    super(CITY_CODES, "NAPA", "CA", 
          "Location:ADDR/S? MUN:CITY? TIME:TIME! EVT_#:ID! EV_TYPE:CALL!");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      pt = field.indexOf(',');
      if (pt >= 0) {
        data.strApt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      
      pt = data.strAddress.indexOf(": alias");
      if (pt >= 0) {
        data.strAddress = data.strAddress.substring(0,pt).trim() + " (" + data.strAddress.substring(pt+7).trim() + ')';
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern CALL_GPS_PTN = Pattern.compile("(.*?) \\(LAT: *([\\d\\.]+) *LON: *(-[\\d\\.]+)\\)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        setGPSLoc(match.group(2) + ',' + match.group(3), data);
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CALL GPS";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
    "AMC",       "AMERICAN CANYON",
    "ANG",       "ANGWIN",
    "CNTY",      "NAPA COUNTY",
    "CNTY CST",  "CALISTOGA COUNTY",
    "CNTY NAPA", "NAPA COUNTY",
    "CST",       "CALISTOGA",
    "DRP",       "DEER PARK",
    "NAPA",      "NAPA",
    "STH",       "ST HELENA",
    "YNT",        "YOUNTVILLE"
  });
  
}
