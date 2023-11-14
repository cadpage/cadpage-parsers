package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class TXParkerCountyDParser extends FieldProgramParser {

  public TXParkerCountyDParser() {
    super("PARKER COUNTY","TX",
          "Add._Codes:CALL! CALL2/SDS+? ADDRCITYST! ( Business:PLACE! | PLACE! ) APT! X MAP! ID! UNIT! UNIT/ZC+? INFO! INFO/+? NAME! ID2/L? EMPTY+? DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@weatherfordtx.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strCall = stripFieldEnd(subject, ";");
    int pt = body.indexOf("\nThis message");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (body.startsWith("{incident_codes_description}; ")) body = "Add. Codes: " + body.substring(30);
    return super.parseFields(body.split("; "), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("MAP")) return new MapField("[A-Z0-9]{3,4}", true);
    if (name.equals("ID")) return new IdField("CFS\\d{9}", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID2")) return new IdField("WFD\\d\\d-\\d{7}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains(",")) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|LOT) *(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt =  match.group(1);
        if (apt == null) apt = field;
        super.parse(field, data);
      }
      else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +(.*)");

  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return true;

      Matcher match = INFO_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strSupp = append(data.strSupp, "\n", match.group(1));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}

