package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * fAlamance county, NC
 */
public class NCPasquotankCountyParser extends DispatchOSSIParser {
  
  public NCPasquotankCountyParser() {
    super("PASQUOTANK COUNTY", "NC",
           "FYI? CALL ADDR INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@co.pasquotank.nc.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.contains(";")) {
      int pt = body.indexOf(":CAD:");
      if (pt < 0) return false;
      pt += 5;
      body = body.substring(0,pt) + subject + ' ' +  body.substring(pt);
    }
    return super.parseMsg(body, data);
  }
}