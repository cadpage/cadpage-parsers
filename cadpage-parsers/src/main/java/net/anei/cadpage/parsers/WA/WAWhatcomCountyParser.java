package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAWhatcomCountyParser extends FieldProgramParser {

  public WAWhatcomCountyParser() {
    super("WHATCOM COUNTY", "WA",
          "Message__to__Active911__call_number:ID! location:ADDR! CITY! latitude:GPS1! longitude:GPS2! call_type:CODE! call_type_desc:CALL! priority:PRI! " +
              "initial_remarks:INFO! dispatched_unit:SKIP! station:SRC! stn_jurisdiction:SKIP! response:UNIT! radio_channel:CH END", FLDPROG_DOUBLE_UNDERSCORE);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }

}
