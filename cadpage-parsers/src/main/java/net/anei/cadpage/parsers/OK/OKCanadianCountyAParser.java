package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKCanadianCountyAParser extends FieldProgramParser {
  
  public OKCanadianCountyAParser() {
    super("CANADIAN COUNTY", "OK",
          "ID:ID! CALL:CALL! INFO:INFO! ADDR:ADDR! CITY:CITY! PLACE:PLACE! LATITUDE:GPS1! LONGITUDE:GPS2! UNIT:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "support@geosafe.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
}
