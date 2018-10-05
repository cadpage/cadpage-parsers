package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;

public class TXSomervellCountyBParser extends FieldProgramParser {
  
  public TXSomervellCountyBParser() {
    super("SOMERVELL COUNTY", "TX", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATETIME! TIME:SKIP! MAP:MAP! UNIT:UNIT! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "zt@co.somervell.tx.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("d\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d");
    return super.getField(name);
  }
}
