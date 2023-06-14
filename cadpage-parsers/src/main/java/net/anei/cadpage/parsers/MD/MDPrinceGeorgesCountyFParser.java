package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDPrinceGeorgesCountyFParser extends FieldProgramParser {

  public MDPrinceGeorgesCountyFParser() {
    super("PRINCE GEORGES COUNTY", "MD",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! UNIT:UNIT! INFO:INFO!");
  }

  @Override
  public String getFilter() {
    return "firema8@ehub33.webhostinghub.com,messaging@firemapping.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(".", "");
      super.parse(field, data);
    }
  }
}
