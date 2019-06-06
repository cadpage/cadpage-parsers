package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;


public class TNOakRidgeParser extends FieldProgramParser {
  
  public TNOakRidgeParser() {
    super("OAK RIDGE", "TN", 
          "ID CALL ADDR! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@oakridgetn.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf("\nElectronic communications ");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID"))  return new IdField("[EF]\\d{2}-\\d+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
}
