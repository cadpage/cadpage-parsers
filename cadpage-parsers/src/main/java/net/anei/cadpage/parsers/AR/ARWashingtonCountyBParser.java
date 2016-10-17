package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARWashingtonCountyBParser extends FieldProgramParser {
  
  public ARWashingtonCountyBParser() {
    this("WASHINGTON COUNTY", "AR");
  }
  
  protected ARWashingtonCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! APT:APT! City:CITY! TIME:TIME! CROSSSTREET:X! NOTE:INFO! LAT:GPS1! LON:GPS2!");
  }
  
  @Override
  public String getAliasCode() {
    return "ARWashingtonCountyB";
  }
  
  @Override
  public String getFilter() {
    return "noreplyActive911@centralems.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Active 911 Alert")) return false;

    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      String[] parts = field.split(" - ", 3);
      if (parts.length == 1) {
        data.strCall = field;
      } else {
        data.strCode = parts[0].trim();
        data.strCall = parts[parts.length-1].trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        data.strCross = append(data.strCross, " / ", part);
      }
    }
  }
}
