package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class ALMarshallCountyDParser extends DispatchA74Parser {
  
  public ALMarshallCountyDParser() {
    super("MARSHALL COUNTY", "AL");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace('|', '\n');
    return super.parseMsg(subject, body, data);
  }
  
}
