package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CALakeCountyBParser extends FieldProgramParser {
  
  public CALakeCountyBParser() {
    super("LAKE COUNTY", "CA", 
          "CALL ADDRCITY ID! Remarks:EMPTY! INFO GPS! Resources:UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "lnucad@fire.ca.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = data.strCity.replace('_', ' ');
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Inc# (\\d+)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("http://maps.google.com/")) return;
      super.parse(field, data);
    }
  }
}
