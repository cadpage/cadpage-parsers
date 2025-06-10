package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXAransasPassParser extends FieldProgramParser {

  public TXAransasPassParser() {
    super("ARANSAS PASS", "TX", "ID ADDRCITY X INFO/N+? ID_UNIT! END");
  }

  @Override
  public String getFilter() {
    return "police@aptx.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strCall =  subject;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("CAD # (\\d+)", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID_UNIT")) return new MyIdUnitField();
    return super.getField(name);
  }

  private static final Pattern PLACE_ADDR_PTN = Pattern.compile("-(.*?)- (.*)");
  private static final Pattern APT_CITY_PTN = Pattern.compile("(\\S*\\d\\S*|[A-Z])\\b (.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPlace = match.group(1).trim();
      super.parse(match.group(2).trim(), data);
      match = APT_CITY_PTN.matcher(data.strCity);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strCity = match.group(2);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains("//")) abort();
      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("(?:[a-z]+: *)?(.*?)(?: +\\[\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d\\])?");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern ID_UNIT_PTN = Pattern.compile("(\\d+)=([-,A-Za-z0-9]+)$");
  private class MyIdUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "ID UNIT";
    }

  }
}
