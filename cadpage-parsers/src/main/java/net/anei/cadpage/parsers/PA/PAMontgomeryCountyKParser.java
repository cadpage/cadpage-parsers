package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAMontgomeryCountyKParser extends FieldProgramParser {

  public PAMontgomeryCountyKParser() {
    super("MONTGOMERY COUNTY", "PA",
          "Address:ADDRCITYST! Facility:PLACE! Specific_Location:APT! Call_Notes:CALL! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@zapiermail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n-----");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String val;
      if ((val = getValue("Patient Name:", field)) != null) {
        data.strName = val;
      } else if ((val = getValue("Call Back Number:", field)) != null) {
        data.strPhone = val;
      } else if ((val = getValue("Unit Dispatched:", field)) != null) {
        data.strUnit = val;
      } else {
        super.parse(field, data);
      }
    }

    private String getValue(String label, String field) {
      if (!field.startsWith(label)) return null;
      return field.substring(label.length()).trim();
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " NAME PHONE UNIT";
    }
  }
}
