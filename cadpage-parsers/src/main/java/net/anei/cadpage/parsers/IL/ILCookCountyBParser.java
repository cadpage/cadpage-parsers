package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILCookCountyBParser extends FieldProgramParser {
  
  public ILCookCountyBParser() {
    super("COOK COUNTY", "IL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch"))  return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{4}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
