package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class OHWarrenCountyDParser extends FieldProgramParser {
  
  public OHWarrenCountyDParser() {
    super(OHWarrenCountyParser.CITY_LIST, "WARREN COUNTY", "OH", 
          "Call:CALL! Location:ADDRCITY/S Priority:PRI Unit:UNIT/S Units:UNIT/S Name:PLACE INFO/N", FLDPROG_IGNORE_CASE | FLDPROG_ANY_ORDER);
  }
  
  @Override
  public String getFilter() {
    return "garrettpopovich.oh@gmail.com,@thebeachwaterpark.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0) data.msgType = MsgType.GEN_ALERT;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Notes:");
      super.parse(field, data);
    }
  }
}
