package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNCarterCountyParser extends FieldProgramParser {

  public TNCarterCountyParser() {
    super("CARTER COUNTY", "TN",
          "CODE CALL CODE CALL PLACE ADDRCITYST GPS1 GPS2 X DATETIME INFO UNIT! END");
  }

  @Override
  public String getFilter() {
    return "noreply@cctn911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d)(?: \\*)?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = mergeFields(data.strCode, field);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = mergeFields(data.strCall, field);
    }
  }

  private static String mergeFields(String oldValue, String newValue) {
    if (newValue.equals("None") || newValue.isEmpty()) return oldValue;
    if (newValue.equals(oldValue)) return oldValue;
    return append(oldValue, " - ", newValue);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }

  }
}
