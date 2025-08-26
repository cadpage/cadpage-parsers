package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORMarionCountyCParser extends FieldProgramParser {

  public ORMarionCountyCParser() {
    super("MARION COUNTY", "OR",
          "Call_Type:CALL! Date/Time:DATETIME! Location:ADDRCITY! Name:NAME! Intersection:X! Lat:GPS1! Long:GPS2! Incident:ID! Units:UNIT! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@cityofsalem.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("911 DISPATCH !")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
