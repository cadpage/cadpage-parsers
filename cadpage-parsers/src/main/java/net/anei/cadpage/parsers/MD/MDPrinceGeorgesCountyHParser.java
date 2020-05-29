package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchFastAlertParser;

public class MDPrinceGeorgesCountyHParser extends DispatchFastAlertParser {
  
  public MDPrinceGeorgesCountyHParser() {
    super("PRINCE GEORGES COUNTY", "MD");
  }
  
  @Override
  public String getFilter() {
    return "volunteer25@co.pg.md.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
