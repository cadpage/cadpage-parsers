package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class ALBlountCountyParser extends DispatchB2Parser {
  
  public ALBlountCountyParser() {
    super(CITY_LIST, "BLOUNT COUNTY", "AL");
    setupCallList((CodeSet)null);
    removeWords("PLACE");
  }
  
  @Override
  public String getFilter() {
    return "BLOUNTCO911@otelco.net";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean good = false;
    if (body.startsWith("BLOUNTCO911:")) {
      good = true;
      body = body.substring(12).trim();
    }
    if (subject.length() > 0) body = '(' + subject + ") " + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strName.startsWith("Bldg")) data.strName = data.strName.substring(4).trim();
    if (data.strApt.endsWith("Bldg")) data.strApt = data.strApt.substring(0,data.strApt.length()-4).trim(); 
    return (good || data.strCallId.length() > 0 || data.msgType == MsgType.RUN_REPORT);
  }

  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@', '&').replace(" AT ", " & ");
    return super.parseAddrField(field, data);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ALLGOOD",
    "ALTOONA",
    "BLOUNTSVILLE",
    "CLEVELAND",
    "COUNTY LINE",
    "GARDEN CITY",
    "HAYDEN",
    "HIGHLAND LAKE",
    "LOCUST FORK",
    "NECTAR",
    "ONEONTA",
    "ROSA",
    "SNEAD",
    "SUSAN MOORE",
    "WARRIOR",
    "BALLE MOBILE HOME PARK",
    "BANGOR",
    "BROOKSVILLE",
    "HOPEWELL",
    "MOUNT HIGH",
    "REMLAP",
    "SMOKE RISE",
    "SUMMIT",
    
    // Jefferson County
    "PINSON",
    "TRAFFORD",
    
    // Marshal Country
    "BOAZ",
    "HORTON",
    
    // St Clair County
    "SPRINGVILLE"
  };
}
