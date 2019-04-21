package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyAParser extends FieldProgramParser {
  
  public ILLakeCountyAParser() {
    super("LAKE COUNTY", "IL",
          "Incident:ID! Nat:CALL! Loc:ADDRCITY! Grid:MAP! Tac:CH! Trucks:UNIT! Notes:INFO!");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\0', '\n');
    return super.parseMsg(body, data);
  }
  
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      parseAddress(field.substring(0, pt).trim(), data);
      data.strCity = field.substring(pt+1).trim();
    }
  }
}
