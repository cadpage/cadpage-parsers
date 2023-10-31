package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXBrazoriaCountyDParser extends DispatchOSSIParser {

  public TXBrazoriaCountyDParser() {
    super("BRAZORIA COUNTY", "TX",
          "( CANCEL ADDR " +
          "| FYI ID UNIT CALL ADDR! " +
          ") INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    return super.getField(name);
  }

}
