package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAStCharlesParishParser extends FieldProgramParser {

  public LAStCharlesParishParser() {
    super("ST CHARLES PARISH", "LA",
          "ADDRCITYST CALL INFO! END");
  }

  @Override
  public String getFilter() {
    return "zuercher@stcharlessheriff.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    String tmp = body;
    body = stripFieldEnd(body, "; Please respond immediately.");
    if (body.length() == tmp.length()) return false;

    data.strCall = subject;
    return parseFields(body.split(";", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (field.equals("Alarm monitoring company")) return;
      data.strCall = field;
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\.)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = INFO_BRK_PTN.matcher(field).replaceAll("\n");
    }
  }
}
