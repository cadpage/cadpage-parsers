package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INMarshallCountyBParser extends FieldProgramParser {

  public INMarshallCountyBParser() {
    super("MARSHALL COUNTY", "IN",
          "CALL ADDRCITYST PLACE? X/Z INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@co.marshall.in.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.endsWith(" Please respond immediately.")) return false;
    body = body.substring(0,body.length()-28).trim();
    if (!parseFields(body.split(";"), data)) return false;
    return subject.equals(data.strCall);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "None");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return true;
      Matcher match = INFO_PTN.matcher(field);
      if (!match.lookingAt()) return false;
      field = field.substring(match.end());
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
