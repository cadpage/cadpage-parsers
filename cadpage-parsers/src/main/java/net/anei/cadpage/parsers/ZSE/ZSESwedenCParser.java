package net.anei.cadpage.parsers.ZSE;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenCParser extends ZSESwedenBaseParser {
  
  public ZSESwedenCParser() {
    super("", "",
          "CALL CALL CALL CALL INFO ADDR CITY UNIT CH GPS! END");
  }

  @Override
  public String getLocName() {
    return "Contal";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
}
