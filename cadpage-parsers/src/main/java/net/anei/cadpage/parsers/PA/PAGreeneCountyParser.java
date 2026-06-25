package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class PAGreeneCountyParser extends FieldProgramParser {

  public PAGreeneCountyParser() {
    super("GREENE COUNTY", "PA",
          "SKIP+ DATE/TIME:DATETIME! CALL_TYPE:CODE_CALL! LOCATION:ADDRCITY! ADDITIONAL_LOCATION:PLACE! CROSS_STREETS:X! COMMON_NAME:PLACE! UNITS:UNIT! NARRATIVE:INFO! INFO/N+ Latitude:GPS1! Longitude:GPS2! ");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.greene.pa.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Dispatch")) return false;
    int pt = body.indexOf("\n________");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- *(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode =  match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
