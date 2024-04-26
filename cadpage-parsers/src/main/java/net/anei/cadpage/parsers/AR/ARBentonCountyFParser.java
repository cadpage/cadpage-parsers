package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyFParser extends FieldProgramParser {

  public ARBentonCountyFParser() {
    super("BENTON COUNTY", "AR",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! ID:ID! PRI:PRI! DATE:DATETIME! MAP:X! UNIT:UNIT! INFO:INFO! INFO/N+? GPS END");
    setupMultiWordStreets("MARTIN LUTHER KING JR");
  }

  @Override
  public String getFilter() {
    return "pditservice@fayetteville-ar.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strInfoURL.isEmpty() &&
          field.startsWith("http:") || field.startsWith("https:")) {
        data.strInfoURL = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " URL";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("LONGITUDE:(.*?) +LATITUDE:(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
