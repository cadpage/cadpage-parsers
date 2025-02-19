package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA99Parser extends FieldProgramParser {

  public DispatchA99Parser(String defCity, String defState) {
    super(defCity, defState,
          "CFS:ID! CALL! ADDRCITYST! GPS PHONE Received:DATETIME! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("^(?:Call Notes:|\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d -[A-Z]+\\b) *| *\\[[A_Z]+\\]$");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

}
