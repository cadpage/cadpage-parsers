package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAErieCountyBParser extends FieldProgramParser {
  
  public PAErieCountyBParser() {
    super("ERIE COUNTY", "PA",
           "CALL ADDR PLACE+? X");
  }
  
  @Override
  public String getFilter() {
    return "wececc@verizon.net,messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Rule out PAErieCountyA messages
    if (body.startsWith("ERIE911:")) return false;
    
    if (body.endsWith(" -")) body = body + ' ';
    String[] flds = body.split(" - ", -1);
    return parseFields(flds, 3, data);
  }
  
  
  private class MyPlaceField extends AptField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("APT ")) {
        data.strApt = append(data.strApt, " - ", field.substring(4).trim());
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  // Cross field only valid if it contains an &
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("&")) return false;
      data.strCross = field;
      return true;
    }
    
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
}
