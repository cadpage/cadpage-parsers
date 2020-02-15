package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MIBarryCountyAParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN =  Pattern.compile("CAD Page for CFS (\\d{6}-\\d{1,3})");
  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?<!\n)(?=http://maps)");
  
  public MIBarryCountyAParser() {
    super("BARRY COUNTY", "MI",
          "CALL_ADDR TIME! INFO+? GPS");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    body = MISSING_BRK_PTN.matcher(body).replaceFirst("\n");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_ADDR")) return new CallAddressField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class CallAddressField extends AddressField {
    @Override 
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt < 0) abort();
      data.strCall = field.substring(0,pt).trim();
      super.parse(field.substring(pt+1).trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL ADDR APT";
    }
  }
  
  private static final String GPS_MARKER = "http://maps.google.com/maps?q="; 
  private static final Pattern GPS_PTN = Pattern.compile("([-+][\\d\\.]+)%20([-+][\\d\\.]+)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      if (field.startsWith(GPS_MARKER)) {
        Matcher match = GPS_PTN.matcher(field.substring(GPS_MARKER.length()));
        if (!match.matches()) return true;
        setGPSLoc(match.group(1) + ',' + match.group(2), data);
        return true;
      }
      return GPS_MARKER.startsWith(field);
    }
  }
}
