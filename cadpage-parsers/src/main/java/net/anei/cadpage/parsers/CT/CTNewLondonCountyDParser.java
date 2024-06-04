package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewLondonCountyDParser extends FieldProgramParser {

  public CTNewLondonCountyDParser() {
    super("NEW LONDON COUNTY", "CT",
          "ID CALL PLACE ADDR APT CITY ZIP X UNIT DATETIME/d GPS1 GPS2 INFO EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "nxgnpaging@waterfordct.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("APT")) return new AptField("Unit# *(.*)", false);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} \\d\\d:\\d\\d:\\d\\d+", true);
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(+1))) return;
      super.parse(field, data);
    }
  }
}
