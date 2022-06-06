package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJMiddlesexCountyFParser extends FieldProgramParser {

  public NJMiddlesexCountyFParser() {
    super("MIDDLESEX COUNTY", "NJ",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:EMPTY! TIME:EMPTY! UNIT:UNIT! INFO:INFO! END");
  }

  @Override
  public String getFilter() {
    return "CommunicationsCenter@rwjbh.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\n", "");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*\\[\\d+\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
