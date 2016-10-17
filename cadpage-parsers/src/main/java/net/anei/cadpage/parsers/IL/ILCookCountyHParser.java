package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILCookCountyHParser extends FieldProgramParser {
  
  public ILCookCountyHParser() {
    super(CITY_CODES, "COOK COUNTY", "IL", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHGO HTS",     "CHICAGO HEIGHTS",
      "HEIGHTS",      "CHICAGO HEIGHTS",
      "OLY FLDS",     "OLYMPIA FIELDS"
  });
}
