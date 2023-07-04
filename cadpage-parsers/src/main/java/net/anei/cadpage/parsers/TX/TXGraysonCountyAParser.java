package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXGraysonCountyAParser extends FieldProgramParser {

  public TXGraysonCountyAParser() {
    super("GRAYSON COUNTY", "TX",
          "DATETIME! CFS:ID_CALL! ADDRCITY! INFO/N+ Narrative:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CADAlerts@cityofdenison.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CALL")) return new MyIdCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ID_CALL_PTN = Pattern.compile("(\\d+) *(?:Type: *)?(.*)");

  private class MyIdCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "ID CALL";
    }
  }
}
