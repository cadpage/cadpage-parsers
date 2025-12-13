package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC03Parser extends FieldProgramParser {

  public DispatchC03Parser(String defCity, String defState) {
    super(defCity, defState,
          "ID ADDRCITY X! INFO INFO/N+? ID_UNIT ID_UNIT+");
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
    if (name.equals("ID")) return new IdField("CAD # (\\d+)", true);
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("ID_UNIT")) return new BaseIdUnitField();
    return super.getField(name);
  }

  private static final Pattern PLACE_ADDR_PTN = Pattern.compile("-(.*?)- (.*)");
  private static final Pattern APT_CITY_PTN = Pattern.compile("(\\S*\\d\\S*|[A-Z])\\b (.*)");
  private class BaseAddressCityField extends AddressCityField {
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

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains("//")) abort();
      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("(?:[a-z]+: *)?(.*?)(?: +\\[(?:\\d\\d/\\d\\d/\\d{4} )?\\d\\d:\\d\\d:\\d\\d\\])?");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern ID_UNIT_PTN = Pattern.compile("(\\d+)=([-,A-Za-z0-9]+)$");
  private class BaseIdUnitField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId = append(data.strCallId, ",", match.group(1));
      data.strUnit = append(data.strUnit, ",", match.group(2));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID UNIT";
    }

  }
}
