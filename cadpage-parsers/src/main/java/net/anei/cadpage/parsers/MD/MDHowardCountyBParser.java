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
    if (!parseFields(body.split("//"), data)) return false;

    if (!data.strCallId.isEmpty()) {
      data.strInfoURL = "https://cad.howardcountymd.gov/details/" + data.strCallId;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " URL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[EF]\\d{8}|OOC\\d{7}", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }
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
