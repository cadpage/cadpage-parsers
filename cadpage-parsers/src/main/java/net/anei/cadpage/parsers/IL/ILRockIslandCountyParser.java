package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ILRockIslandCountyParser extends FieldProgramParser {

  public ILRockIslandCountyParser() {
    super("ROCK ISLAND COUNTY", "IL",
          "Primary_Code:CALL! Secondary_Code:CALL/SDS! Address:ADDRCITYST! Location:PLACE! Time:DATETIME! Details:INFO! END");
  }

  @Override
  public String getFilter() {
    return "rico911-notify@ricoetsb.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - (?:Remarks - )? *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }
}
