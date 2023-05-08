package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAGreeneCountyBParser extends FieldProgramParser {

  public VAGreeneCountyBParser() {
    super("GREENE COUNTY", "VA",
          "ID! Nat:CALL! Add:ADDR! Resp_Units:UNIT! Comments:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "40808-N9hZUEFtuQYvYVRd@alert.active911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INC # +(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
