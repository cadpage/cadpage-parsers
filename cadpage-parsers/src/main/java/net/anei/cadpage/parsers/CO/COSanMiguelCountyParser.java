package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COSanMiguelCountyParser extends FieldProgramParser {
  
  public COSanMiguelCountyParser() {
    super(CITY_CODES, "SAN MIGUEL COUNTY", "CO", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO! INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MON", "Montrose County",
      "MOU", "Mountain Village",
      "OPH", "Ophir",
      "RED", "Redvale",
      
      "DEN", "Denver"
  });

}
