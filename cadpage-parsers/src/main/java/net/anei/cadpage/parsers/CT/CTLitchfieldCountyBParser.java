package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTLitchfieldCountyBParser extends FieldProgramParser {
  
  public CTLitchfieldCountyBParser() {
    super("LITCHFIELD COUNTY", "CT", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! CROSS_STREET_1:X? CROSS_STREET_2:X? INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "winchesterpddispatch@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" X ", " & ");
      field = stripFieldStart(field, "X ");
      super.parse(field,  data);;
    }
  }
}
