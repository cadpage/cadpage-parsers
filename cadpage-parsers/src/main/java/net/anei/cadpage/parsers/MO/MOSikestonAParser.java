package net.anei.cadpage.parsers.MO;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOSikestonAParser extends FieldProgramParser {
 
  public MOSikestonAParser() {
    super("SIKESTON", "MO",
          "PLACE UNK? TIME ADDR CALL!");
  }
  
  @Override
  public String getFilter() {
    return "cad_incident@sikeston.org";
  }
  
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d?:\\d\\d?", true);
    return super.getField(name);
  }
}