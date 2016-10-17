package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDBinghamCountyParser extends FieldProgramParser {
  
  public IDBinghamCountyParser() {
    super("BINGHAM COUNTY", "ID", 
          "ID:ID! CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! LAT:GPS1! LONG:GPS2! PRI:PRI! DATE:DATE! TIME:TIME! INFO:INFO! INFO/N+ UNIT:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "cad@co.bingham.id.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

}
