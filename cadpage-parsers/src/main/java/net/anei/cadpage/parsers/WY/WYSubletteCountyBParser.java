package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WYSubletteCountyBParser extends FieldProgramParser {
  
  public WYSubletteCountyBParser() {
    super("SUBLETTE COUNTY", "WY", 
          "CALL ADDR X GPS! INFO END");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X"))  return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      data.strPlace = p.getLastOptional(';');
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Between:");
      field = stripFieldStart(field, "Intersection of:");
      super.parse(field, data);
    }
  }
}
