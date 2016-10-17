package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNHubbardCountyParser extends FieldProgramParser {
  
  public MNHubbardCountyParser() {
    super("HUBBARD COUNTY", "MN", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY? ID:ID! INFO:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

}
