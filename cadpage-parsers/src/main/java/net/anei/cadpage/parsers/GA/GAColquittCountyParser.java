package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GAColquittCountyParser extends FieldProgramParser {

  public GAColquittCountyParser() {
    super("COLQUITT COUNTY", "GA", 
          "Call_Type:CALL! Date:DATETIME! Location:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! Agency_Dispatched:SRC! Units_Currently_Assigned:UNIT! EMPTY+? GPS?");
  }
  
  @Override
  public String getFilter() {
    return "911alert@interoponline.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("InterOp CAD Alert - ")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new GPSField("Estimated Maps Location.*'http://maps.apple.com/maps\\?q=([^']*?)'.*", true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "||");
      super.parse(field, data);
    }
  }

}

