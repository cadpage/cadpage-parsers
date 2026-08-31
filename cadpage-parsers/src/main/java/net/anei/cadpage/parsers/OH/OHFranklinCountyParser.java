package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHFranklinCountyParser extends FieldProgramParser {
  
  public OHFranklinCountyParser() {
    super("FRANKLIN COUNTY", "OH", 
         "ADDRCITY PLACE DATETIME CALL! UNIT CH END");
  }

  @Override 
  public String getFilter() {
    return "dispatch@grovecityohio.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
