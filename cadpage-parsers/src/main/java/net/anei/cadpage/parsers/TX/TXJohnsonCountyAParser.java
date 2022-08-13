package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXJohnsonCountyAParser extends DispatchOSSIParser {

  public TXJohnsonCountyAParser() {
    super("JOHNSON COUNTY", "TX", "( CANCEL ADDR | FYI? SRC UNIT? CALL ADDR X+? INFO+ )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{1,3}", true);
    if (name.equals("UNIT"))  return new UnitField("[A-Z]+\\d+", true);
    return super.getField(name);
  }
}
