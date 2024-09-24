package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INHancockCountyCParser extends FieldProgramParser {

  public INHancockCountyCParser() {
    super("HANCOCK COUNTY", "IN",
          "PLACE:PLACE! ADDR:ADDR! CROSS_STREETS:X! FIRE_TYPE:CALL! UNIT:UNIT! ALARM_LEVEL:PRI! INFO:INFO! FIRE_RD:BOX! " +
              "PRIMARY_INCIDENT:ID! GPS_LAT:GPS1! GPS_LON:GPS2! MARK! NARRATIVE:INFO/N! INFO/N+ Call_Type:SKIP! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("MARK")) return new SkipField("#{10,}", true);
    return super.getField(name);
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("::");
      String fld1 = field.substring(0,pt).trim();
      String fld2 = field.substring(pt+2).trim();
      if (fld1.equals(fld2)) field = fld1;
      super.parse(field, data);
    }
  }

}
