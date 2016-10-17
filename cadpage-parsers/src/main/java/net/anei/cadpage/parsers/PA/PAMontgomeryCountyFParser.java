package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAMontgomeryCountyFParser extends FieldProgramParser {
  
  public PAMontgomeryCountyFParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
           "CALL UNIT ADDR CITY X ID DISPATCH TIME PLACE UNK UNK MAP! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "alerts@royersfordfd.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.endsWith("~")) body = body + " ";
    body = body.replace('\n', ' ');
    return parseFields(body.split(" ~ "), 12, data);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = setGPSLoc(field, data);
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("F\\d+", true);
    if (name.equals("DISPATCH")) return new SkipField("Dispatch", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MapField("|\\d\\d-\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
