package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class ALHooverAParser extends FieldProgramParser {

  public ALHooverAParser() {
    super("HOOVER", "AL",
          "CFS:SKIP! CALL:CALL! DATE:DATETIME! PLACE:PLACE! ADDR:ADDRCITY/S6! CROSS:X! RUN_#:ID! UNIT:UNIT! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatchNWPS@ci.hoover.al.us,dispatchNWPS@hooveralabama.gov,arns@shelby911.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!CAD Alert!")) return false;
    body = body.replace(" CALL:", "\nCALL:");
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
