package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAGreensvilleCountyBParser extends FieldProgramParser {

  public VAGreensvilleCountyBParser() {
    super("GREENSVILLE COUNTY", "VA",
          "Call_Time:DATETIME! Incident_Type:CALL! CFS_Number:ID! Caller:NAME! Street:ADDRCITYST! Cross_Road:X! Details:INFO! Responding_Units:UNIT! END");
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    return super.parseMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("NAME")) return new MyNameField();
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

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - (?:Add Remarks - )? *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String part : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part.trim());
      }
    }
  }
}
