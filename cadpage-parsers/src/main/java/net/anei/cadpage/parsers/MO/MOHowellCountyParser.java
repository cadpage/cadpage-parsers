package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOHowellCountyParser extends FieldProgramParser {
  
  public MOHowellCountyParser() {
    super("HOWELL COUNTY", "MO", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! X_STREET:X! NOTE:INFO% INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cad@howellcounty911.com,4173729057";
  }
    
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '/');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
