package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class COMontroseCountyParser extends FieldProgramParser {
  
  public COMontroseCountyParser() {
    super("MONTROSE COUNTY", "CO",
           "CALL:CALL! ADDR:ADDR! PLACE:PLACE? CITY:CITY ID:ID UNIT:UNIT INFO:INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
    }
}
