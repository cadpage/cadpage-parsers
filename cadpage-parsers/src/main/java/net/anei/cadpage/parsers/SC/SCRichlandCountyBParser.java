package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCRichlandCountyBParser extends FieldProgramParser {

  public SCRichlandCountyBParser() {
    super("RICHLAND COUNTY", "SC",
          "CALL ADDR PLACE CITY ST GPS UNIT! END");
  }

  @Override
  public String getFilter() {
    return "noreply@alastar.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fort Jackson Response Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
}
