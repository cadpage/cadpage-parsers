
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCLeeCountyParser extends DispatchSouthernParser {

  public SCLeeCountyParser() {
    super(CITY_LIST, "LEE COUNTY", "SC", DSFLAG_ID_OPTIONAL | DSFLAG_FOLLOW_CROSS);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@leecountysc.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCross = stripFieldEnd(data.strCross, " INTERSECTION");
    return true;
  }


  private static final String[] CITY_LIST = new String[]{
      "BISHOPVILLE",
      "DALZELL",
      "LYNCHBURG",
      
      // Darlington County
      "HARTSVILLE",
      
      // Kershaw County
      "CAMDEN",
      
      // Sumpter County
      "REMBERT"
  };
}
