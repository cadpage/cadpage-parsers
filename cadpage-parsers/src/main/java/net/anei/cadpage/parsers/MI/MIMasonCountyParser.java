package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIMasonCountyParser extends FieldProgramParser {

  public MIMasonCountyParser() {
    super("MASON COUNTY", "MI",
          "GPS1 GPS2 CALL PLACE ADDRCITYST ID! INFO INFO/N+? PHONE! UNIT END");
  }

  @Override
  public String getFilter() {
    return "csproreports@gmail.com,ossicad@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d\\d\\.\\d{6}|None");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_HDR_PTN.matcher(field).replaceFirst("");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyPhoneField extends PhoneField {
    public MyPhoneField() {
      super("\\d{3}-\\d{3}-\\d{4}|None", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    public MyUnitField() {
      super("[A-Z]+\\d+|None", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
