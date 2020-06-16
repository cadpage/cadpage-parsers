package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA77Parser extends FieldProgramParser {
  
  private String marker;
  private Properties cityCodes;
  
  public DispatchA77Parser(String marker, Properties cityCodes, String defCity, String defState) {
    super(defCity, defState, 
          "CALL ADDRCITY! ( CALL/SDS Cross_Street:X GPS1 GPS2 " +
                         "| Cross_Street:X GPS1 GPS2 " +
                         "| GPS1 GPS2 " +
                         "| CALL/SDS GPS1 GPS2 " +
                         ") END");
    this.marker = marker;
    this.cityCodes = cityCodes;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(marker)) return false;
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
      data.strCity = convertCodes(p.getLastOptional(':'), cityCodes);
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
