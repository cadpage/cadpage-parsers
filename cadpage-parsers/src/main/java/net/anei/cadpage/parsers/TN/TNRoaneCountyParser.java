package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;


public class TNRoaneCountyParser extends DispatchA71Parser {
  
  public TNRoaneCountyParser() {
    super("ROANE COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "maupindk2@gmail.com,pagegate1@roanecounty911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("[-+]\\d{2,3}\\.\\d{6} +[-+]\\d{2,3}\\.\\d{6}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_GPS_PTN.matcher(field).matches()) {
        setGPSLoc(field, data);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
}
