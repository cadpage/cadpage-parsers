package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAHumboldtCountyBParser extends FieldProgramParser {
  
  public CAHumboldtCountyBParser() {
    super("HUMBOLDT COUNTY", "CA", 
          "AUTOMATIC_CAD_TEXT%EMPTY! INCIDENT_TYPE:CALL! DATETIME! EMPTY PLACE ADDR CITY EMPTY! CROSS_STREET:X! EMPTY GPS! EMPTY INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cadpaging@ci.eureka.ca.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatched Call ")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Map in Google")) abort();
      super.parse(field, data);
    }
  }
}
