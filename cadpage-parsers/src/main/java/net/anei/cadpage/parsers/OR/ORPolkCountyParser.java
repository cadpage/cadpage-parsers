package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORPolkCountyParser extends FieldProgramParser {
  
  public ORPolkCountyParser() {
    super("POLK COUNTY", "OR",
          "CALL SRC DATETIME ADDR/SP UNIT ID!");
  }
  
  @Override
  public String getFilter() {
    return "wvccsupdesk@cityofsalem.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), 6, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{12}", true);
    return super.getField(name);
  }
}
