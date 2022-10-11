package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNHamblenCountyParser extends FieldProgramParser {

  public TNHamblenCountyParser() {
    super("HAMBLEN COUNTY", "TN",
          "CALL_UNIT ADDR ( PLACE X/Z TIME! " +
                         "| X/Z? TIME! " +
                         ") END");
  }

  @Override
  public String getFilter() {
    return "CAD@hamblen911.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_UNIT")) return new MyCallUnitField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyCallUnitField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(' ');
      if (pt < 0) abort();
      data.strCall =  field.substring(0,pt).trim();
      data.strUnit = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }
}
