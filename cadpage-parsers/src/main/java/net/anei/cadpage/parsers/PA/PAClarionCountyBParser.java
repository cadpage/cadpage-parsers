package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;


public class PAClarionCountyBParser extends DispatchA17Parser {
  
  public PAClarionCountyBParser() {
    super("CLARION COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "ccoes-cad@oes.clarion.pa.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    PAClarionCountyParser.fixCity(data);
    return true;
  }
}
