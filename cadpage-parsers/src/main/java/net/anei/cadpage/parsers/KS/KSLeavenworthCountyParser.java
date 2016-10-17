package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

/**
 * Leavenworth County, KS
 */
public class KSLeavenworthCountyParser extends DispatchProphoenixParser {
    
  public KSLeavenworthCountyParser() {
    super("LEAVENWORTH COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "FireDispatch@firstcity.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n#######");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
  
}
