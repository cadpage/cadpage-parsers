package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAClallamCountyCParser extends FieldProgramParser {

  public WAClallamCountyCParser() {
    super("CLALLAM COUNTY", "WA",
          "CALL:CALL! ADDR:ADDRCITY/S6! ID:ID! PRI:PRI! DATE:DATETIME! UNIT:UNIT! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
