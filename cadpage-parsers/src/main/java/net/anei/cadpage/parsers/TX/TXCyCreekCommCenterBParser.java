package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCyCreekCommCenterBParser extends FieldProgramParser {
  
  public TXCyCreekCommCenterBParser() {
    super("HARRIS COUNTY", "TX", 
          "ADDR:ADDR! APT:APT! PLACE:PLACE! X-ST:X! MAP:MAP! SUB:CITY! NATURE:CALL! PRI:PRI! UNITS:UNIT! LAT:GPS1! LON:GPS2! ID:ID! CN:INFO");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" CN - ", " CN: ");
    return super.parseMsg(body, data);
  }
  
}
