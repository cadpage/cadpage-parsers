package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHFairfieldCountyParser extends FieldProgramParser {

  public OHFairfieldCountyParser() {
    super("FAIRFIELD COUNTY", "OH",
          "CALL ADDR ( X! | CITY X! | CITY ST X! ) INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.fairfield.oh.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.endsWith(",")) body += ' ';
    return parseFields(body.split(", "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("//")) return false;
      field = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
