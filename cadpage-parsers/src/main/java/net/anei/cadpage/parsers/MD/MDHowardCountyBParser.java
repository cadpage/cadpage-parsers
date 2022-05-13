package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDHowardCountyBParser extends FieldProgramParser {
  
  public MDHowardCountyBParser() {
    super("HOWARD COUNTY", "MD", 
          "ID CALL PLACE ADDR CITY APT UNIT TIME! END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" //"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[EF]\\d{8}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
