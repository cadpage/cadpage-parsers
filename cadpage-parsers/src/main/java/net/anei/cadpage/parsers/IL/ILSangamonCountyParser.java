package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Ocean City County, NJ
 */
public class ILSangamonCountyParser extends DispatchA9Parser {
  
  public ILSangamonCountyParser() {
    super(null, "SANGAMON COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // They just put city information in the map field
    data.strMap = "";
    return true;
  }
}
