package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Delaware County, IN
 */
public class INDelawareCountyParser extends FieldProgramParser {
  
  public INDelawareCountyParser() {
    super("DELAWARE COUNTY", "IN", 
          "NOC:EMPTY! Status:SKIP! Call_Type:CALL! Alarm_Level:PRI! Address:ADDRCITY! City:CITY! Common_Name:PLACE! Closest_Intersections:X! Quad:MAP! Units:UNIT! Narrative:INFO! INFO/N+ Coord:GPS! Inc_#:ID! END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.delaware.in.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("APCO Intellicomm Ems:")) return;
      if (field.equals("CAD Information")) return;
      if (field.startsWith("Caller Name:")) {
        data.strName = field.substring(12).trim();
      } else if (field.startsWith("Caller Phone:")) {
        data.strPhone = field.substring(13).trim();
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('/', ',');
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " NAME PHONE";
    }
  }
}
