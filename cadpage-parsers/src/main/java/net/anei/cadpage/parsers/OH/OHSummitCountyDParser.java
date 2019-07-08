package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Summit County, OH
 */
public class OHSummitCountyDParser extends FieldProgramParser {
  
  public OHSummitCountyDParser() {
    super("SUMMIT COUNTY", "OH",
          "CALL_LOCATION:ADDRCITY! COMMON_NAME:PLACE! CALL_TYPE:CALL! TIME_OF_CALL:DATETIME! INCIDENT_NUMBER:ID! NARRATIVE:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@police.kent.edu";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}