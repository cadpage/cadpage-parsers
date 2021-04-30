package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXEllisCountyCParser extends FieldProgramParser {

  public TXEllisCountyCParser() {
    super("ELLIS COUNTY","TX",
          "ADDRCITYST CALL X UNIT! INFO EMPTY END");
  }

  @Override
  public String getFilter() {
    return "alerts@waxahachie.com";
  }

  private static final Pattern DELIM = Pattern.compile("\\*(?: +|$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active Call Alert")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *(?:; *)?\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
