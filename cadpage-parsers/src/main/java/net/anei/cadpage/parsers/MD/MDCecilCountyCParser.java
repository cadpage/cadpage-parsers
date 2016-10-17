package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCecilCountyCParser extends FieldProgramParser {
  
  public MDCecilCountyCParser() {
    super("CECIL COUNTY", "MD",
        "TYPE:CALL! LOC:ADDR! BOX:BOX! TIME:TIME!");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(" - ");
      String addr = p.get('@');
      String place = p.get();
      
      // They aren't consistent about whether the place comes before or after the address
      // so we will pick the one that doesn't look like an address
      if (place.length() > 0) {
        if (checkAddress(place) > checkAddress(addr)) {
          String tmp = addr;
          addr = place;
          place = tmp;
        }
      }
      data.strPlace = place;
      super.parse(addr, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CITY";
    }
  }
  
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() != 6) return;
      field = field.substring(0,2) + ':' + field.substring(2,4) + ':' + field.substring(4,6);
      super.parse(field, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }  
}






