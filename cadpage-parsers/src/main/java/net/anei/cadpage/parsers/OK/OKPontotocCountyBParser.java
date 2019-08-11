package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKPontotocCountyBParser extends FieldProgramParser {

  public OKPontotocCountyBParser() {
    super("PONTOTOC COUNTY", "OK", 
          "Call:CALL! ADDR:ADDR! CITY:CITY! UNIT:UNIT! INFO:INFO/N");
  }
  
  @Override
  public String getFilter() {
    return "niblettnathan@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
}
