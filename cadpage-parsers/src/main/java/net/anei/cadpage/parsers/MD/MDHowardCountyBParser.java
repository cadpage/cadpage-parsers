package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDHowardCountyBParser extends FieldProgramParser {

  public MDHowardCountyBParser() {
    super("HOWARD COUNTY", "MD",
          "ID CALL PLACE ADDR CITY APT UNIT TIME! BOX END");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("//"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[EF]\\d{8}|OOC\\d{7}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
}
