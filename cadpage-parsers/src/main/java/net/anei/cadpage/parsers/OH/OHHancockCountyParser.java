package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHHancockCountyParser extends FieldProgramParser {

  public OHHancockCountyParser() {
    super("HANCOCK COUNTY", "OH",
          "CALL ADDR CITY! ST? X INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@co.Hancock.oh.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, ",");
    return parseFields(body.split(","), 3, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field  = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
