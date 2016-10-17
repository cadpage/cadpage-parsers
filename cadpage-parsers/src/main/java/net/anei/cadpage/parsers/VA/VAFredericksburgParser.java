package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VAFredericksburgParser extends DispatchOSSIParser {
  
  public VAFredericksburgParser() {
    super("FREDERICKSBURG", "VA", 
          "( CANCEL ADDR! SKIP INFO+ | FYI? ( ADDR/Z ID UNIT? CALL2! X+? INFO+ | PLACE ADDR/Z ID UNIT? CALL2! X+? INFO+ | PLACE? CALL ADDR/Z END ) )");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("CALL2")) return new CallField("[A-Z]+", true);
    return super.getField(name);
  }
}
