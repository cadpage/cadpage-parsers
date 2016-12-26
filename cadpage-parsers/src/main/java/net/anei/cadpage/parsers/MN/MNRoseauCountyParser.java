package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNRoseauCountyParser extends FieldProgramParser {
  
  public MNRoseauCountyParser() {
    super("ROSEAU COUNTY", "MN", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/S6! CITY:CITY? ID:ID! INFO:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
}
