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
    return "cad@howellcounty911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("PowerPhone CACH:") ||
          field.equals("Call started") ||
          field.equals("Call closed")) return;
      super.parse(field, data);
    }
  }
}
