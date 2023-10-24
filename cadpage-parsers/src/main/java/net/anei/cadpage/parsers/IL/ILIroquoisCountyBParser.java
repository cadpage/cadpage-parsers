package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILIroquoisCountyBParser extends FieldProgramParser {

  public ILIroquoisCountyBParser() {
    super(ILIroquoisCountyParser.CITY_LIST, "IROQUOIS COUNTY", "IL",
          "CALL ( ADDRCITYST/Z INFO! Common_name:PLACE! CAD_Notes:INFO/N! New_Message:INFO! " +
               "| PLACE ADDR/Z CITY/Y! " +
               "| ADDR/Z CITY/Y! " +
               "| PLACE? ADDR! " +
               ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "co.iroquois.il.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
