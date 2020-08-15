package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA79Parser extends FieldProgramParser {

  private String reqSubject;

  public DispatchA79Parser(String reqSubject, String defCity, String defState) {
    super(defCity, defState,
          "Unit:UNIT! Run:ID! Patient:NAME! INFO/N+? Pickup:EMPTY! " +
          "( PLACE ADDR/Z APT/Z CITY_ST_ZIP! | ADDR/Z CITY_ST_ZIP! | ADDR/Z APT CITY_ST_ZIP | PLACE ADDR CITY_ST_ZIP! | ADDR APT CITY_ST_ZIP! ) INFO/N+");
    this.reqSubject =  reqSubject;
  }

  private static final Pattern PREFIX = Pattern.compile("Dispatch Message - (Alert|At Destination) - ");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (reqSubject != null && !subject.equals(reqSubject)) return false;

    Matcher match = PREFIX.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCall = match.group(1);
    body = body.substring(match.end()).trim();

    if (data.strCall.equals("At Destination")) data.msgType = MsgType.RUN_REPORT;

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("CITY_ST_ZIP")) return new BaseCityStateZipField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE|UNIT) +(.*)");
  private class BaseAptField extends AptField {

    public BaseAptField() {
      super("\\S*|.*,.*|.*\bFLOOR\b.*(?:APT|GATE|ER|RM|ROOM|SUITE|UNIT) .*", false);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([^,]+?), *([A-Z]{2})(?: +\\d{5})?");
  private class BaseCityStateZipField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = match.group(1).trim();
      data.strState = getOptGroup(match.group(2));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
