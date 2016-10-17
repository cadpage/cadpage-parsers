package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyFiretracker3Parser extends FieldProgramParser {
  
  public NYNassauCountyFiretracker3Parser() {
    super("NASSAU COUNTY", "NY", 
          "Call_Type:CALL! Additional_Info:CALL/SDS Address:ADDR! Between:X CountyNum:ID! DOA:DATE! TOA:TIME! FDID:SRC!");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@firetracker.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("FirePage")) return false;
    if (!body.startsWith("FPC FD Call ")) return false;
    if (!body.endsWith("[FireTracker]")) return false;
    body = body.substring(12, body.length()-13).trim();
    
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{12}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("] /[", "/");
      field = stripFieldStart(field, "[");
      field = stripFieldEnd(field, "]");
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" and ", " / ");
      super.parse(field, data);
    }
  }

}
