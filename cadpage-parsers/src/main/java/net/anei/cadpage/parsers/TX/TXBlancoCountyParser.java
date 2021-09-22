package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBlancoCountyParser extends FieldProgramParser {

  public TXBlancoCountyParser() {
    super("BLANCO COUNTY", "TX",
          "CALL:CALL! ADDR:ADDRCITYST! ID:ID! PRI:PRI! UNIT:UNIT! INFO:INFO!");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.blanco.tx.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_DELIM_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

}
