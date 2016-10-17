package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NJBurlingtonCountyGParser extends FieldProgramParser {
  
  public NJBurlingtonCountyGParser() {
    super("BURLINGTON COUNTY", "NJ",
           "TIME ID CALL ADDR X INFO UNIT!");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    return super.getField(name);
  }
}
