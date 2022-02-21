package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYGreenupCountyAParser extends FieldProgramParser {

  public KYGreenupCountyAParser() {
    super("GREENUP COUNTY", "KY",
          "UNIT UNIT/C+? CODE_CALL ADDR APT CITY ID! NAME END");
  }

  @Override
  public String getFilter() {
    return "GCE911@greenupe911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    if (!body.startsWith("GCE911:")) return false;
    body = body.substring(7);
    return parseFields(body.split("\t"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]{2,6}", true);
    if (name.equals("CODE_CALL")) return new CodeCallField();
    if (name.equals("APT")) return new AptField("(?i)(?:APT|RM|ROOM|STE) *(.*)|(.*)");
    if (name.equals("ID")) return new IdField("\\d{12}", true);
    return super.getField(name);
  }

  private class CodeCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt < 0) return false;
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }
}
