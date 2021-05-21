package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCAndersonCountyDParser extends FieldProgramParser {

  public SCAndersonCountyDParser() {
    super("ANDERSON COUNTY", "SC",
          "DATETIME CODE CALL ADDRCITYST INFO UNIT ID! NAME EMPTY? PHONE EMPTY END");
  }

  @Override
  public String getFilter() {
    return "centralsquare@andersonsheriff.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    return parseFields(body.split("\\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID")) return new IdField("CFS\\d{4}-\\d+", true);
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^|; *)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line.trim());
      }
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile("; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }
}
