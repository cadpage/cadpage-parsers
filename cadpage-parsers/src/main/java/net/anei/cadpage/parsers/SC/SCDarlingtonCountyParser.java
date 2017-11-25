
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCDarlingtonCountyParser extends DispatchSouthernParser {

  public SCDarlingtonCountyParser() {
    super(CITY_LIST, "DARLINGTON COUNTY", "SC", DSFLAG_FOLLOW_CROSS);
  }
  
  
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
    
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) return false;
    body = body.substring(4).trim();
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
    "CLYDE",
    "DARLINGTON",
    "HARTSVILLE",
    "LAMAR",
    "NORTH HARTSVILLE",
    "SOCIETY HILL",
    
    // Florence County
    "FLORENCE",
    "TIMMONSVILLE",
    
    // Lee County
    "LEE COUNTY",
    "BISHOPVILLE"
  };
}
