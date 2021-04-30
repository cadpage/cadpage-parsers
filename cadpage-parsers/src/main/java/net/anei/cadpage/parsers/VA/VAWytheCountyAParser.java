package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Wythe County, VA
 */
public class VAWytheCountyAParser extends FieldProgramParser {
  
  public VAWytheCountyAParser() {
    super("WYTHE COUNTY", "VA", 
          "CALL! ADDR! ID! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "RIPNRUN@WYTHECO.ORG";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{6}", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
}