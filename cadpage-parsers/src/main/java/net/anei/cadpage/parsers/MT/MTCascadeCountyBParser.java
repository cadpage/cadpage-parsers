package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTCascadeCountyBParser extends FieldProgramParser {
  
  public MTCascadeCountyBParser() {
    super("CASCADE COUNTY", "MT", 
          "DATETIME CALL ( GPS1 GPS2 | ADDR ) ( ID! | ST_ZIP ID! | CITY ST_ZIP? ID! ) ID/S+? NONE Map:MAP! Call_Details:INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "911Text@911intn.org,@greatfallsmt.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nCity of Great Falls");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace("SEARCH & RESCUE", "S&R");
    if (!parseFields(body.split(", "), data)) return false;
    if (subject.length() > 0 && 
        (data.strCall.length() == 0 || data.strCall.equals("WEATHER"))) {
      data.strCall = subject;
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ST_ZIP")) return new MyStateZipField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("NONE")) return new SkipField("None", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyStateZipField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      Matcher match = ST_ZIP_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strState = match.group(1);
//      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(2));
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "ST CITY?";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d+\\.\\d+");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strGPSLoc.length() > 0) {
        data.strAddress = data.strGPSLoc;
        data.strGPSLoc = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(?:\\b[A-Z&]{2,4}\\d{2}-\\d{5}\\b(?:; )?)+");
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) return false;
      field = field.replace("; ", " ");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String info = "";
      for (String line : field.split("; ")) {
        line = line.trim();
        if (line.equals("None")) continue;
        line = INFO_DATE_TIME_PTN.matcher(line).replaceFirst("");
        info = append(info, "\n", line);
      }
      super.parse(info, data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("Gore Hill")) return "Great Falls";
    if (city.equalsIgnoreCase("MANG")) return "Great Falls";
    if (city.equalsIgnoreCase("Sand Coulee")) return "Great Falls";
    return city;
  }
}
