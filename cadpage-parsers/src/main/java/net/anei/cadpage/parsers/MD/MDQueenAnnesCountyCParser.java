package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDQueenAnnesCountyCParser extends FieldProgramParser {

  public MDQueenAnnesCountyCParser() {
    super("QUEEN ANNES COUNTY", "MD",
          "CALL ADDRCITY PLACE X UNIT CH INFO! DATETIME END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@qac.gov,@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!") && !subject.equals("CMalert")) return false;
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
    return super.getField(name);
  }

}
