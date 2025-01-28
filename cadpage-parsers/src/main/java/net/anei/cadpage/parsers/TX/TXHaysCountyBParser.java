package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHaysCountyBParser extends FieldProgramParser {

  public TXHaysCountyBParser() {
    super("HAYS COUNTY", "TX",
          "DATETIME CALL ADDRCITY! MAP ID INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Alert@active911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("(\\d{4}-\\d{8}) +\\(\\S+\\)", true);
    return super.getField(name);
  }
}
