package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAStaffordCountyParser extends FieldProgramParser {


  public VAStaffordCountyParser() {
    super("STAFFORD COUNTY", "VA",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE TIME:TIME UNIT:UNIT! INFO:INFO MAP:MAP LAT:GPS1 LON:GPS2");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }
}
