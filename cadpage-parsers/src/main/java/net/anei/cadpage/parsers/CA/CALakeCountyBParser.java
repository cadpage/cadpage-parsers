package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CALakeCountyBParser extends FieldProgramParser {

  public CALakeCountyBParser() {
    super("LAKE COUNTY", "CA",
          "CALL ADDRCITY Cross-Street:X? ID! Remarks:EMPTY! INFO GPS! Resources:UNIT! INFO/N+");
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
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("Inc# (\\d+)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field =  field.substring(pt+1).trim();
      }
      if (field.endsWith(")")) {
        pt = field.indexOf('(');
        data.strPlace = append(data.strPlace, " - ", field.substring(pt+1, field.length()-1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }

  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("http://maps.google.com/")) return;
      super.parse(field, data);
    }
  }
}
