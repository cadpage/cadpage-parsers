package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA43Parser extends FieldProgramParser {
  
  public DispatchA43Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! ADDR:ADDRCITY/SXa CITY:CITY ID:ID! PRI:PRI INFO:INFO");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("Lat \\(([-+]?\\d+\\.\\d+)\\) Lon \\(([-+]?\\d+\\.\\d+)\\) +(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
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
