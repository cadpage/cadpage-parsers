package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLEscambiaCountyAParser extends FieldProgramParser {

  public FLEscambiaCountyAParser() {
    super("ESCAMBIA COUNTY", "FL",
          "( SELECT/R Rep#:SKIP! TIMES/RN+ | REP#:SKIP! BLDG:PLACE! LOC:ADDR! APT:APT! XST:X! NAT:CALL! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "no-reply@myescambia.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(Assigned to Incident|Incident Closed) (\\d{10})");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) {
      body = stripFieldStart(body, "/ ");
      int pt = body.indexOf(" / ");
      if (pt < 0) return false;
      subject = body.substring(0, pt).trim();
      body = body.substring(pt+3).trim();
    }
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    setSelectValue(match.group(1).startsWith("I") ? "R" : "P");
    data.strCallId = match.group(2);
    if (!parseFields(body.split("\n"), data)) return false;
    String call = FLEscambiaCountyParser.CALL_CODES.getCodeDescription(data.strCode);
    if (call != null) data.strCall = call;
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern CALL_PTN = Pattern.compile("(\\d\\d?[A-Z]\\d\\d?[A-Z]?) - +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("([ A-Za-z]+):?(\\d\\d:\\d\\d:\\d\\d)");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) field = match.group(1) + "  " + match.group(2);
      super.parse(field, data);
    }
  }
}
