package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYRocklandCountyIParser extends FieldProgramParser {

  public NYRocklandCountyIParser() {
    super("ROCKLAND COUNTY", "NY",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! CALLERNUMBER:PHONE! UNIT:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "monseyactive911@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE"))  return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
