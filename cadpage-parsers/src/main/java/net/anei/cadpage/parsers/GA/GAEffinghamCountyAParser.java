package net.anei.cadpage.parsers.GA;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class GAEffinghamCountyAParser extends DispatchA74Parser {
  
  public GAEffinghamCountyAParser() {
    super("EFFINGHAM COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "effingham911@effinghamcounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nThis e-mail");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
  
}
