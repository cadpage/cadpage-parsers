package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLCalhounCountyParser extends FieldProgramParser {

  public FLCalhounCountyParser() {
    super("CALHOUN COUNTY", "FL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP? UNIT:UNIT! INFO:INFO!");
  }

  @Override
  public String getFilter() {
    return "woody@usa-software.com,calhouncad@gmail.com,CalhounCAD@CalhounSheriff.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) {
        field = adjustAddress(data.strPlace);
        data.strPlace = "";
      } else {
        if (field.equals(data.strPlace)) data.strPlace = "";
        field = adjustAddress(field);
        if (field.equals(data.strPlace)) data.strPlace = "";
      }
      super.parse(field, data);
    }

    private String adjustAddress(String  addr) {
      addr = stripFieldEnd(addr, " 0");
      addr = addr.replace("HAYES S/D RD", "HAYES SUBDIVISION RD");
      return addr;
    }
  }
}
