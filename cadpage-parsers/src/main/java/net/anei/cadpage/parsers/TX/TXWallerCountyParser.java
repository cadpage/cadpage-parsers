package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXWallerCountyParser extends FieldProgramParser {

  public TXWallerCountyParser() {
    super("WALLER COUNTY", "TX",
          "( ADDR:ADDR! APT:APT! PLACE:PLACE! X-ST:X! MAP:MAP! SUB:MAP/L! NATURE:CALL! PRI:PRI! UNITS:UNIT! LAT:GPS1! LON:GPS2! ID:ID! NOTES:INFO! " +
          "| CALL:CALL! CALLS:SKIP! ADDR:ADDR! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! X:X INFO:INFO " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains("\n")) {
      return parseFields(body.split("\n"), data);
    } else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ',');
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
