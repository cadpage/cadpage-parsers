package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GAHenryCountyBParser extends DispatchH05Parser {
  
  public GAHenryCountyBParser() {
    super("HENRY COUNTY", "GA", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY INFO_BLK+");
  }
  
  @Override
  public String getFilter() {
    return "@co.henry.ga.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
