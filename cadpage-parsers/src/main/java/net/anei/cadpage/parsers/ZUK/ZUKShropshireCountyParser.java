package net.anei.cadpage.parsers.ZUK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZUKShropshireCountyParser extends FieldProgramParser {
  
  public ZUKShropshireCountyParser() {
    super("SHROPSHIRE COUNTY", "", CountryCode.UK, 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! INFO/N+ LAT:GPS1 LON:GPS2");
  }
  
  @Override
  public String getFilter() {
    return "@medaidservices.co.uk,@medaidgroup.net";
  }

  @Override
  public String getLocName() {
    return "Shropshire County, UK";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\t', ' ');
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(". (,)", "\n").trim();
      super.parse(field, data);
    }
  }
}
