package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Trumble County, OH 
 */
public class OHTrumbullCountyParser extends FieldProgramParser {
  
  public OHTrumbullCountyParser() {
    super("TRUMBULL COUNTY", "OH", 
           "ADDR APT EMPTY+? CALL! EMPTY+? SRC EMPTY+? X EMPTY+? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "911no@co.trumbull.oh.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("*")) return false;
    return parseFields(body.substring(1).trim().split("\\*"), data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.equals("OH")) city = p.getLastOptional(',');
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
}
