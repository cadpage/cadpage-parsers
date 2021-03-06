package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class WACowlitzCountyParser extends FieldProgramParser {
  
  public WACowlitzCountyParser() {
    super(CITY_CODES, "COWLITZ COUNTY", "WA",
          "LOC:ADDR/S! RMRK:INFO! CALLER_NAME:NAME CALLER_ADDR:SKIP TIME:TIME% Disp:UNIT%");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean subjectColonField() { return true; }
    };
  }

  @Override
  public String getFilter() {
    return "postmaster@cowlitz911.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0 && !subject.equals("DIVE")) {
      body = append(subject+':', " ", body);
    }
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strUnit.length() == 0) data.expectMore = true;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        String place = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
        String city = CITY_CODES.getProperty(field);
        if (city != null) {
          parseAddress(place, data);
          data.strCity = city;
          return;
        }
        data.strPlace = place;
      }
      
      String apt = "";
      pt = field.lastIndexOf(',');
      if (pt < 0) pt = field.lastIndexOf(';');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]?\\d+\\.\\d{6} +[-+]?\\d+\\.\\d{6})[- ]+(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      // Look for GPS coordinates
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "AM", "AMBOY",
      "AR", "ARIEL",
      "CO", "COUGAR",
      "CR", "CASTLE ROCK",
      "KA", "KALAMA",
      "KE", "KELSLO",
      "LV", "LONGVIEW",
      "RV", "ROSE VALLEY",
      "SL", "SILVER LAKE",
      "TO", "TOUTLE",
      "WD", "WOODLAND"
  });

}
