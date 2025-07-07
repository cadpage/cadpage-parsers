package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYMcCrackenCountyBParser extends FieldProgramParser {

  public KYMcCrackenCountyBParser() {
    super("MCCRACKEN COUNTY", "KY",
          "CALL ADDRCITY X X PLACE? ID! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply@paducahky.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    if (body.endsWith("/")) body += ' ';
    return parseFields(body.split(" / "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8} \\(.*\\)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("911NOT")) return;
      super.parse(field, data);
    }
  }
}
