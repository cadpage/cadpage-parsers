package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IACerroGordoCountyParser extends FieldProgramParser {
  
  public IACerroGordoCountyParser() {
    super("CERRO GORDO COUNTY", "IA",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY ID:ID! INFO:INFO+");
  }
  
  @Override
  public int getMapFlags() {
    
    // The GPS coordinates are not necessary all that good, but when they are present
    // the only address is a street name, so go with the GPS coordinates
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field  getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("Lat \\(([-+]?\\d{1,3}\\.\\d{5})\\) Lon \\(([-+]?\\d{1,3}\\.\\d{5})\\) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strGPSLoc = match.group(1)+','+match.group(2);
        if (data.strGPSLoc.equals("0.00000,0.00000")) data.strGPSLoc = "";
        field = match.group(3);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
}
