package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMahoningCountyBParser extends FieldProgramParser {

  public OHMahoningCountyBParser() {
    super("MAHONING COUNTY", "OH",
          "UNIT CALL ADDR CITY DATETIME X! ID INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{5}", true);
    return super.getField(name);
  }

}
