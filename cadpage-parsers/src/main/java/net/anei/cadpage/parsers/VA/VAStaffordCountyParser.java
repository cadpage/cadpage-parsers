package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;


public class VAStaffordCountyParser extends FieldProgramParser {
  
  
  public VAStaffordCountyParser() {
    super("STAFFORD COUNTY", "VA",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO LAT:GPS1 LON:GPS2");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
