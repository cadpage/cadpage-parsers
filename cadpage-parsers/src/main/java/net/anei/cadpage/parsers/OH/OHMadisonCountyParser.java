package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Madison County, OH
 */
public class OHMadisonCountyParser extends DispatchA1Parser {

  public OHMadisonCountyParser() {
    super("MADISON COUNTY", "OH"); 
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0 && body.startsWith("Alert:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String getFilter() {
    return "mcsocad@madisonsheriff.org,777";
  }
}
