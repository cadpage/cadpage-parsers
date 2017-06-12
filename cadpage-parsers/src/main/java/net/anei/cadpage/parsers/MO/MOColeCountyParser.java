package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cole County, MO
 */
public class MOColeCountyParser extends FieldProgramParser {
  
  public MOColeCountyParser() {
    super("COLE COUNTY", "MO",
      "UNIT:UNIT! ZONE:MAP! PRI:PRI! PLACE:PLACE? ADDR:ADDR! CITY:CITY? CALL:CALL! INFO:INFO? INFO+? ID:ID! DATE:DATE! TIME:TIME! GPS:GPS");
  }
  
  @Override
  public String getFilter() {
    return "ccems";
  }
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split(""), data);
  }
}
