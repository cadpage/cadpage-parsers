package net.anei.cadpage.parsers.NY;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYOrangeCountyDParser extends FieldProgramParser {
  
  public NYOrangeCountyDParser() {
    super("ORANGE COUNTY", "NY",
        "CALL! ADDR! CITY! XST:X");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("sCAD")) return false;
    body = body.replace("XST:", "|XST:");
    if (!parseFields(body.split("\\|"), data)) return false; 
    return true;
  }
    private class MyCityField extends CityField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt > 0) {
        data.strSupp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
     
    @Override
    protected Field getField(String name) {
      if (name.equals("CITY")) return new MyCityField();
      return super.getField(name);
    }
}

