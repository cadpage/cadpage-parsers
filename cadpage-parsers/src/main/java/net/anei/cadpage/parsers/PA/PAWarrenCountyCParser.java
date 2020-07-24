package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWarrenCountyCParser extends FieldProgramParser {

  public PAWarrenCountyCParser() {
    super("WARREN COUNTY", "PA",
          "Call_Time:DATETIME! Call_Type:CALL_CODE! Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! Additional_Location_Info:INFO! Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! Priority:PRI! Quadrant:MAP! District:MAP/L! Beat:MAP/L! CFS_Number:ID! Primary_Incident:ID/L! Radio_Channel:CH! Narrative:INFO!");
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!NTR!")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL_CODE")) return new MyCallCodeField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(.*) -(\\d+)");
  private class MyCallCodeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

}
