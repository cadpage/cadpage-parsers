package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXDallasCountyGParser extends DispatchOSSIParser {
  
  public TXDallasCountyGParser() {
    super("DALLAS COUNTY", "TX", 
          "( CANCEL ADDR SKIP! " + 
          "| FYI? ID MAP UNIT CALL ADDR X X CITY PLACE CH! " + 
          ") INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    return super.getField(name);
  }
}
