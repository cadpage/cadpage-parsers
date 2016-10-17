package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNSibleyCountyParser extends FieldProgramParser {
  
  public MNSibleyCountyParser() {
    super("SIBLEY COUNTY", "MN", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY ID:ID! PRI:PRI! INFO:INFO/N+");
  }

  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
}
