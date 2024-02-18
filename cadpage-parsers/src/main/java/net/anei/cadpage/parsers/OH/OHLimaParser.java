package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLimaParser extends FieldProgramParser {

  public OHLimaParser() {
    super("LIMA", "OH",
          "Call_Date/Time:DATETIME! Dispatched_Units:UNIT! Fire/EMS_Call_Type:CODE_CALL! Nature_of_Call:CALL/SDS! Call_Location:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Quadrant:MAP! Incident#:ID! Desc:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "lpd-dispatch@cityhall.lima.oh.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split(";\n*|\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d+\\S+) *- *(.*?)(?: - .*)?");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) {
        data.strCall = field;
      } else {
        data.strCode = match.group(1);
        data.strCall = match.group(2).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
