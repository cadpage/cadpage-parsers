package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORYamhillCountyAParser extends FieldProgramParser {

  public ORYamhillCountyAParser() {
    super("YAMHILL COUNTY", "OR",
          "Call_Type:CALL! Landmark:PLACE! Address:ADDR! Units:UNIT! Notes:INFO! CAD_#:ID! END");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ,]+(?=\\[\\d+\\])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
