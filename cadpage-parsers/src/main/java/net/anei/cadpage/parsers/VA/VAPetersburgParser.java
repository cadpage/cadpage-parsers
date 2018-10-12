package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAPetersburgParser extends DispatchOSSIParser {
  
  public VAPetersburgParser() {
    super("PETERSBURG", "VA",
          "( CANCEL ADDR SKIP! | FYI CALL PRI? ADDR! X X ) INFO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@petersburg-police.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }
}
