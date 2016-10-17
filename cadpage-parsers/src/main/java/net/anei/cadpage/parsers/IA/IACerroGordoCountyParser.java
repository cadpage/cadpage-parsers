package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IACerroGordoCountyParser extends FieldProgramParser {
  
  public IACerroGordoCountyParser() {
    super("CERRO GORDO COUNTY", "IA",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY! ID:ID! INFO:INFO+");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split(";"), data);
  }
}
