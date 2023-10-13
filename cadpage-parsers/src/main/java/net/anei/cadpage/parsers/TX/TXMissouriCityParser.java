package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Missouri City, TX
 */
public class TXMissouriCityParser extends DispatchOSSIParser {
  public TXMissouriCityParser() {
    super("MISSOURI CITY", "TX",
        "FYI? CALL ADDR UNIT? INFO/N+? ID END");
  }

  public String getFilter() {
    return "CAD@missouricitytx.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:"+ body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|\\S+,\\S+", true);
    if (name.equals("ID")) return new IdField("\\d{13}", true);
    return super.getField(name);
  }
}
