package net.anei.cadpage.parsers.MO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MOCrawfordCountyAParser extends FieldProgramParser {
  
  private static final Pattern REPORT_MARKER = Pattern.compile("^\\*\\*\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d \\d{3}");
 
  public MOCrawfordCountyAParser() {
    super(CITY_LIST, "CRAWFORD COUNTY", "MO",
          "CALL PLACE ADDR/S INFO+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    String save = body;
    int pt = body.indexOf('\n');
    String extra = null;
    if (pt >= 0) {
      extra = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }
    String[] flds = body.split(" / ");
    if (flds.length < 7) return false;
    if (extra != null && REPORT_MARKER.matcher(extra).find()) {
      data.strCall = "RUN REPORT";
      data.strPlace = save;
      return true;
    }
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("^P, *([A-Z]*[0-9]+(?:[ ,] *[A-Z]*[0-9]+)*)\\b");
  private static final Pattern RECVD_PTN = Pattern.compile("^Call Received on (\\d\\d/\\d\\d/\\d{4}) @ (\\d\\d:\\d\\d)\\b");
  private class MyInfoField extends InfoField {
    @Override
    public void  parse(String field, Data data) {
      Matcher match = RECVD_PTN.matcher(field);
      if (match.find()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        field = field.substring(match.end()).trim();
      }
      match = UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = append(data.strUnit, ",", match.group(1));
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static String[] CITY_LIST = new String[]{
    "BERRYMAN",
    "BOURBON",
    "CHERRYVILLE",
    "COOK STATION",
    "CUBA",
    "DAVISVILLE",
    "DILLARD",
    "FANNING",
    "HUZZAH",
    "LEASBURG",
    "ST. CLOUD",
    "STEELVILLE",
    "WESCO",
    "WEST SULLIVAN"
  };
}