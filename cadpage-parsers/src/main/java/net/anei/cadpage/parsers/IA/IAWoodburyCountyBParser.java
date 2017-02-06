package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWoodburyCountyBParser extends FieldProgramParser {
  
  public IAWoodburyCountyBParser() {
    super("WOODBURY COUNTY", "IA", 
          "ID DATE/d TIME CALL ADDR CITY PLACE? X/Z+? INFO/Z UNIT/SZ EMPTY/Z! END");
  }
  
  @Override
  public String getFilter() {
    return "WCICC3@sioux-city.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}");
    if (name.equals("DATE")) return new DateField("\\d\\d-\\d\\d-\\d\\d");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    public boolean checkParse(String field, Data data) {
      if (data.strAddress.endsWith(" INTERSECTN")) {
        data.strAddress = data.strAddress.substring(0, data.strAddress.length()-11).trim();
        return false;
      }
      parse(field, data);
      return true;
    }
  }
  
  private static final String INFO_UNIT_TAG = "The following units paged:";
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf("The");
      if (pt >= 0) {
        if (INFO_UNIT_TAG.startsWith(field.substring(pt))) {
          field = field.substring(0,pt).trim();
        }
      }
      pt = field.indexOf(INFO_UNIT_TAG);
      if (pt >= 0) {
        data.strUnit = field.substring(pt+INFO_UNIT_TAG.length()).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO UNIT";
    }
  }
}
