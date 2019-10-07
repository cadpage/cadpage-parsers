package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALColbertCountyCParser extends FieldProgramParser {

  public ALColbertCountyCParser() {
    super("COLBERT COUNTY", "AL", 
          "ID CALL ADDR UNIT END");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0 && body.startsWith("//")) {
      body = "From CAD(" + subject + ") " + body;
    }
    return parseFields(body.split(" // "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("From CAD\\((\\d+)\\)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get(';'), data);
      data.strApt = stripFieldStart(data.strApt, "APT");
      
      String place = p.get(';');
      if (place.startsWith("APT")) {
        data.strApt = append(data.strApt, "-", place.substring(3).trim());
      } else {
        data.strPlace = place;
      }
      data.strSupp = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE INFO";
    }
  }
}
