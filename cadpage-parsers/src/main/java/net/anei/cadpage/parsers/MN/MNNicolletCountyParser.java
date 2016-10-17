package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNNicolletCountyParser extends FieldProgramParser {
  
  public MNNicolletCountyParser() {
    super("NICOLLET COUNTY", "MN", 
          "CALL:CALL! ADDR:ADDR/S6! CITY:CITY ID:ID! INFO:INFO/N+");
    setupSpecialStreets("CHAPEL VIEW");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
}
