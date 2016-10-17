package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDPrinceGeorgesCountyFParser extends FieldProgramParser {
  
  public MDPrinceGeorgesCountyFParser() {
    super("PRINCE GEORGES COUNTY", "MD", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! UNIT:UNIT! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "firema8@ehub33.webhostinghub.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

}
