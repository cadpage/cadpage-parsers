package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOScottCountyBParser extends FieldProgramParser {
  
  public MOScottCountyBParser() {
    super("SCOTT COUNTY", "MO", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
