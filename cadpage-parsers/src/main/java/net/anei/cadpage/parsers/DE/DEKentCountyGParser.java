package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DEKentCountyGParser extends FieldProgramParser {

  public DEKentCountyGParser() {
    super("KENT COUNTY", "DE",
          "CALL ADDRCITY! Xst's:X! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "64CAD@amb64.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Reject DEKentCountyE alerts
    if (body.startsWith("Call Type:")) return false;

    if (NUMERIC.matcher(subject).matches()) data.strCallId = subject;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)[- ]+(.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
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

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
