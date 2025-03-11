package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLimaParser extends FieldProgramParser {

  public OHLimaParser() {
    super("LIMA", "OH",
          "Call_Time:DATETIME! Call_Type:CODE_CALL! Address:ADDRCITY! Latitude:GPS1! Longitude:GPS2! Common_Name:PLACE! " +
              "Closest_Intersection:X! Additional_Location_Info:INFO! Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! " +
              "Priority:PRI! Status:SKIP! Quadrant:MAP! District:MAP/L! Beat:MAP/L! CFS_Number:SKIP! Primary_Incident:ID! " +
              "Radio_Channel:CH! Narrative:INFO/N! INFO/N+ Dispatched:SKIP! Caller_Name:NAME! Caller_Phone:PHONE! EOF! END");
  }

  @Override
  public String getFilter() {
    return "lpd-dispatch@cityhall.lima.oh.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split(";\n*|\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("EOF")) return new SkipField("<EOF>", true);
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
