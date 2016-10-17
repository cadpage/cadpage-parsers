package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAPetersburgParser extends DispatchOSSIParser {
  
  public VAPetersburgParser() {
    super("PETERSBURG", "VA",
          "( CANCEL ADDR SKIP! | FYI CALL ADDR! X X ) INFO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@petersburg-police.com";
  }
}
