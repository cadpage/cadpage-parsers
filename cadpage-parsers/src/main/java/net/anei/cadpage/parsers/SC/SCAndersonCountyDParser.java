package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCAndersonCountyDParser extends FieldProgramParser {

  public SCAndersonCountyDParser() {
    super("ANDERSON COUNTY", "SC",
          "DATETIME CODE CALL ADDRCITYST INFO INFO? UNIT ID! ( NAME PHONE PHONE/CS | ) GPS1 GPS2 END");
  }

  @Override
  public String getFilter() {
    return "centralsquare@andersonsheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID_NAME")) return new MyIdNameField();
    if (name.equals("ID")) return new IdField("CFS\\d{4}-\\d+", true);
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^|; *)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line.trim());
      }
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile("; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern ID_NAME_PTN =  Pattern.compile("(CFS\\d{4}-\\d+) +(.*)");
  private class MyIdNameField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_NAME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId =  match.group(1);
      String name = match.group(2);
      if (!name.equals("None")) data.strName = cleanWirelessCarrier(name);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID NAME";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6}");

  private class MyNameField extends NameField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (GPS_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
