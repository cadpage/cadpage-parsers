package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INBooneCountyCParser extends FieldProgramParser {
  
  public INBooneCountyCParser() {
    super("BOONE COUNTY", "IN", 
          "CALL PAGED? ADDRCITY UNIT MAP! INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PAGED")) return new SkipField("PAGED", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|BNE|NMF|RAM", true);
    if (name.equals("MAP")) return new MapField("\\d{4}[A-Z]?|0", true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      data.strCity = convertCodes(city, CITY_CODES);
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "COR", "CORYDON",   // In Harrison county
      "DEP", "DEPAUW",        // Harrison County
      "ELI", "ELIZABETH",
      "LAC", "LACONIA",
      "NSA", "NEW SALISBURY", // Harrison county????
      "RAM", "RAMSEY"        // Harrison County
  });

}
