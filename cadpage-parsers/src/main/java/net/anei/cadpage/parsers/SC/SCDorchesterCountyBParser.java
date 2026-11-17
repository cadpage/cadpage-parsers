package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCDorchesterCountyBParser extends FieldProgramParser {

  public SCDorchesterCountyBParser() {
    super("DORCHESTER COUNTY", "SC",
          "ADDR CALL INFO/N+? UNIT! UNIT/ZC+? ID!");
  }

  @Override
  public String getFilter() {
    return "summervillesc@email.getrave.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("SUMMERVILLE FIRE DEPT")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new InfoField("\\d{4}-\\d\\d?-\\d\\d?T\\d\\d:\\d\\d\\:\\d\\d.\\d{3}Z +(.*)", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|DCEMS", true);
    if (name.equals("ID")) return new IdField("FD\\d+", true);
    return super.getField(name);
  }
}
