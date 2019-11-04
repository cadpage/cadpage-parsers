package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class NCMaconCountyDParser extends DispatchA48Parser {

  public NCMaconCountyDParser() {
    super(NCMaconCountyParser.CITY_LIST, "MACON COUNTY", "NC", FieldType.GPS_PLACE_X);
  }
  
  @Override
  public String getFilter() {
    return "2183500429";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "MACON CO 911:");
    return super.parseMsg(subject, body, data);
  }
  
}
