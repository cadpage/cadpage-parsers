package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJAtlanticCountyAParser extends FieldProgramParser {

  public NJAtlanticCountyAParser() {
    super("ATLANTIC COUNTY", "NJ", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/iS6! CITY:CITY! CROSS:X! MAP:MAP DATE:DATETIME INFO:INFO! ID:ID! GPS:GPS");
    removeWords("LA", "NEW", "TERRACE");
    setupSpecialStreets("ATLANTIC GARDENS PARK");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ehtpd.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH") && !subject.equals("Dispatch") &&
        !subject.equals("Phoenix Notification")) return false;

    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}");
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(';'), CITY_CODES);
      String apt = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private Properties CITY_CODES = buildCodeTable(new String[]{
      "EH", "EGG HARBOR"
  });
}
