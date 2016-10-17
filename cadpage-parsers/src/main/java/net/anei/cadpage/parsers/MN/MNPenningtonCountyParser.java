package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Pennington County, MN
 */

public class MNPenningtonCountyParser extends FieldProgramParser {
  
  public MNPenningtonCountyParser() {
    super("PENNINGTON COUNTY", "MN", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY? ID:ID! PRI:PRI? GPS:GPS? INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field.substring(0,1), data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Latitude ");
      field = field.replace(" Longitude ", ",");
      super.parse(field, data);
    }
  }
}
