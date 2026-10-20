package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHFayetteCountyParser extends FieldProgramParser {

  public OHFayetteCountyParser() {
    super("FAYETTE COUNTY", "OH",
          "ADDRCITYST EMPTY ID EMPTY X EMPTY CODE EMPTY CALL! INFO");
  }

  @Override
  public String getFilter() {
    return "zuercherDoNotReply@fayette-co-oh.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("DISPATCH:")) return false;
    return parseFields(body.split(" \\*", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{2,3}\\d\\d-\\d+", true);
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]{0,3}|[A-Z]{0,6}|WELFARE CHECK", true);
    return super.getField(name);
  }
}
