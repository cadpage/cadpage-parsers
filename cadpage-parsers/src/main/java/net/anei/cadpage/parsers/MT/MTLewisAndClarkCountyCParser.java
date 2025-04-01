package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTLewisAndClarkCountyCParser extends FieldProgramParser {

  public MTLewisAndClarkCountyCParser() {
    super("LEWIS AND CLARK COUNTY", "MT",
          "ID ADDRCITYST CODE CALL CODE/L CALLDATETIME INFO UNIT EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@helenamt.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return parseFields(body.split("\\|",-1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{3}");
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALLDATETIME")) return new MyCallDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern CALL_DATE_TIME_PTN = Pattern.compile("(.*?) +(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d)");
  private class MyCallDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      String call = match.group(1);
      if (!call.equals("None")) data.strCall = append(data.strCall, " / ", call);
      super.parse(match.group(2), data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - Log - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
