package net.anei.cadpage.parsers.general;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class StandardSpottedDogParser extends FieldProgramParser {
  
  public StandardSpottedDogParser() {
    super("", "",
           "UNIT CALL ADDR MAP! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@response.spotteddogtech.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), 4, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strState = p.getLast(',');
      data.strCity = p.getLast(',');
      parseAddress(p.get(), data);
      if (data.strAddress.length() == 0) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("R Map ")) {
        field = field.substring(6).trim();
        Parser p = new Parser(field);
        data.strMap = p.get(' ');
        field = p.get();
      }
      data.strSupp = field;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
}
