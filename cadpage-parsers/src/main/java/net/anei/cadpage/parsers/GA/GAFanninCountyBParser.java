package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAFanninCountyBParser extends FieldProgramParser {

  public GAFanninCountyBParser() {
    super("FANNIN COUNTY", "GA",
          "ID CALL NAME ADDRCITYST! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "fannindispatch@fannincountyga.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")) return false;
    return parseFields(body.split(" :(?= )"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{7}|[A-Z]{2,3}\\d{2}-\\d+", true);
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
