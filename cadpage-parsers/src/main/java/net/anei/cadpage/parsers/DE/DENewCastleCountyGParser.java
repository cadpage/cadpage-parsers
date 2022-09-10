package net.anei.cadpage.parsers.DE;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DENewCastleCountyGParser extends FieldProgramParser {

  public DENewCastleCountyGParser() {
    super("NEW CASTLE COUNTY", "NJ",
          "Call_Time:DATETIME! Type:CALL! Loc:ADDRCITY! Narr:INFO! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
