package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class VAWarrenCountyAParser extends FieldProgramParser {

  public VAWarrenCountyAParser() {
    super("WARREN COUNTY", "VA",
          "DATETIME CALL ADDRCITY PLACE X INFO/N+? UNIT! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.endsWith(":")) return false;
    body = stripFieldEnd(body, ":");
    return parseFields(body.split(":\n"), data);
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|[A-Z]{3,5})\\b,?)+", true);
    return super.getField(name);
  }
}