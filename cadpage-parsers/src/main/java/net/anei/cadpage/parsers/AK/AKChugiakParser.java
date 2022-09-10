package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class AKChugiakParser extends FieldProgramParser {
  
  public AKChugiakParser() {
    super("CHUGIAK", "AK", 
          "ADDR MAP CALL! INFO/N+ END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("Map +(.*)");
    return super.getField(name);
  }

}
