package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLOkaloosaCountyCParser extends FieldProgramParser {

  public FLOkaloosaCountyCParser() {
    super("OKALOOSA COUNTY", "FL",
          "Units:UNIT! Call_Type:CALL! Loc.Name:PLACE! Area:MAP! Street:ADDR! X.Street:X! Apt#:APT! Bld#:APT2! END");
  }

  @Override
  public String getFilter() {
    return "monitor@firstinalerting.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Alert:")) return false;
    body = body.replace(" Call Type:", "\nCall Type:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("APT")) return new MyApt1Field();
    if (name.equals("APT2")) return new MyApt2Field();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("n/a")) return;
      super.parse(field, data);
    }
  }

  private class MyApt1Field extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("n/a")) return;
      super.parse(field, data);
    }
  }

  private class MyApt2Field extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("n/a")) return;
      data.strApt = append(field, "-", data.strApt);
    }
  }
}
