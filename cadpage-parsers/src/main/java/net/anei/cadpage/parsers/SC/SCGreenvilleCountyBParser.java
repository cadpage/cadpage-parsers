package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCGreenvilleCountyBParser extends FieldProgramParser {
  
  public SCGreenvilleCountyBParser() {
    super("GREENVILLE COUNTY", "SC", 
          "CALL ADDR CITY XINFO! END");
  }
  
  @Override
  public String getFilter() {
    return "active911@parkerfd.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith(subject+';')) return false;
    
    int pt = body.indexOf("<img src=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("XINFO")) return new MyCrossInfoField();
    return super.getField(name);
  }
  
  private class MyCrossInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Between ")) {
        field = field.substring(8).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field, data);
        field = getLeft();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X " + super.getFieldNames();
    }
  }
}
