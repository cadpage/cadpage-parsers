package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDBaltimoreCountyCParser extends FieldProgramParser {

  public MDBaltimoreCountyCParser() {
    super("BALTIMORE COUNTY", "MD",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! BOX:BOX! INFO:INFO! INFO/N+ END");
  }

  @Override
  public String getFilter() {
    return "cadsys@baltimorecountymd.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("LIVE CAD System automated mail message")) return false;
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
      public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
}
