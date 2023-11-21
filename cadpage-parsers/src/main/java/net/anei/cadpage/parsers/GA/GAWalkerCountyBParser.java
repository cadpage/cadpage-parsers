package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAWalkerCountyBParser extends FieldProgramParser {

  public GAWalkerCountyBParser() {
    super("WALKER COUNTY", "GA",
          "CALL CALL_EXT/CS? ADDR CITY X X/CS? UNIT/Z! Quadrant:MAP! GPS! ID! END");
  }

  @Override
  public String getFilter() {
    return "communications@walkerga.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    body = body.replace(" Quadrant:", ", Quadrant:");
    return parseFields(body.split(", "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_EXT")) return new CallField("Leak or Odor");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9,]*", true);
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID")) return new IdField("#(\\S+?)(?: \\([A-Z]*\\d+\\))?", true);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      data.strCross = append(data.strCross, ", ", field);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace('/', ','), data);
    }
  }
}
