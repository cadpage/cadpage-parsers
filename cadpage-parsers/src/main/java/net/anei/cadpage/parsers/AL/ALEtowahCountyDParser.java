package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALEtowahCountyDParser extends FieldProgramParser {

  public ALEtowahCountyDParser() {
    super("ETOWAH COUNTY", "AL",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/SXP! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP? UNIT:UNIT! INFO:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@attallacity.org,no-reply@angeltracksoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n--\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
}
