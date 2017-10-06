package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCIredellCountyBParser extends FieldProgramParser {
  
  public NCIredellCountyBParser() {
    super(CITY_CODES, "IREDELL COUNTY", "NC", 
          "( CALL ADDR/Z CITY/Y INFO! END " +
          "| CALL PRI ADDR! X X UNIT END )");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("[P1-9]?", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MOR", "MOORESVILLE",
      "OLI", "OLIN TWP"
  });

}
