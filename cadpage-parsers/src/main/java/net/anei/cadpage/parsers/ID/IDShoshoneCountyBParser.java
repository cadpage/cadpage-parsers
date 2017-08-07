package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDShoshoneCountyBParser extends FieldProgramParser {
  
  public IDShoshoneCountyBParser() {
    super("SHOSHONE COUNTY", "ID", 
          "ID:ID! CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! LAT:GPS1! LONG:GPS2! PRI:PRI! DATE:DATE! TIME:TIME! INFO:INFO! INFO/N+ UNIT:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "active911@shoshoneso.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
