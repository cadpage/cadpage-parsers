package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA91Parser extends FieldProgramParser {

  private String marker;

  public DispatchA91Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA91Parser(String marker, String defCity, String defState) {
    super(defCity, defState,
          "CALL_TIME ADDR_MAP ( UNIT_INFO! | PLACE X/Z UNIT_INFO! | ( X | PLACE ) UNIT_INFO! ) Sent_by:SKIP! END");
    this.marker = marker;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (marker != null) {
      if (body.startsWith(marker)) {
        body = body.substring(marker.length()).trim();
      } else {
        if (!subject.startsWith(marker)) return false;
      }
    }
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_TIME")) return new BaseCallTimeField();
    if (name.equals("ADDR_MAP")) return new BaseAddressMapField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("UNIT_INFO")) return new BaseUnitIdField();
    return super.getField(name);
  }

  private static final Pattern CALL_TIME_PTN = Pattern.compile("(.*) (\\d\\d:\\d\\d:\\d\\d)");
  private class BaseCallTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strTime = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL TIME";
    }
  }

  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*?) +Dt: *(\\S*) +Zne: *(\\S*) +Gd: *(\\S*)");
  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\d+[A-Z]?)-(\\d+.*)");
  private class BaseAddressMapField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1).trim();
      data.strMap = append(append(match.group(2), "-", match.group(3)), "-", match.group(4));
      match = APT_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        field = match.group(2);
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT ADDR MAP";
    }
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "in ");
      super.parse(field, data);
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("in ")) return false;
      if (field.contains("/") || field.contains("&")) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }

  private static final Pattern UNIT_INFO_PTN = Pattern.compile("(.+?) #(\\d+)");
  private class BaseUnitIdField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_INFO_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).trim();
      data.strCallId = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
  }
}
