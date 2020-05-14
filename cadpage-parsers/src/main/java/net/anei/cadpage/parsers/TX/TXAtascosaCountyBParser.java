package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXAtascosaCountyBParser extends FieldProgramParser {
  
  public TXAtascosaCountyBParser() {
    super("ATASCOSA COUNTY", "TX", 
          "CALL ADDRCITY! Cross_Street:X? GPS1 GPS2 END");
  }
  
  @Override
  public String getFilter() {
    return "so-noreply@acso-tx.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rapid Notification")) return false;
    return parseFields(body.split("\\|"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(':'), TXAtascosaCountyParser.CITY_CODES);
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
    
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);;
    }
  }
  
  private class MyGPSField extends GPSField {
    MyGPSField(int type) {
      super(type, "[-+]?\\d{2,3}\\.\\d{6,}|-361", true);
    }
  }
}
