package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXFranklinCountyParser extends DispatchA72Parser {
  
  public TXFranklinCountyParser() {
    super("FRANKLIN COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "franklin@co.franklin.tx.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
