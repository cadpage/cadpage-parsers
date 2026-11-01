package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWarrenCountyCParser extends FieldProgramParser {

  public IAWarrenCountyCParser() {
    super("WARREN COUNTY", "IA",
          "( RR_MARK/R CALL ADDR INFO NAME TIME! " +
          "| ID CALL ADDR TIME_UNIT NAME! INFO " +
          ") END");
  }

  private static final Pattern DELIM = Pattern.compile(" /(?! [^/])");

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('/');
    if (pt < 0) return false;
    body = body.substring(0,pt) + ' ' + body.substring(pt);
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("RR_MARK")) return new SkipField("INCIDENT COMPLETED", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("TIME_UNIT")) return new MyTimeUnitField();
    return super.getField(name);
  }

  private static final Pattern TIME_UNIT_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)/((?:\\b\\d{4}\\b,?)+)");

  private class MyTimeUnitField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = TIME_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      data.strUnit = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "TIME UNIT";
    }

  }
}
