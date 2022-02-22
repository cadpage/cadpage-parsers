package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyFParser extends FieldProgramParser {

  public ARBentonCountyFParser() {
    super("BENTON COUNTY", "AR",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! MAP:X! UNIT:UNIT! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "pditservice@fayetteville-ar.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
