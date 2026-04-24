package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Lake County, MI
 */
public class MILakeCountyParser extends FieldProgramParser {

  public MILakeCountyParser() {
    super("LAKE COUNTY", "MI",
          "GPS1 GPS2 CALL PLACE ADDRCITYST ID INFO/N+? PHONE/Z EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "csproreports@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";", -1), data);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-}]?\\d{2}\\.\\d{6,}|None");

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN);
    if (name.equals("ID")) return new IdField("[A-Z]+\\d{2}-\\d{5}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PHONE")) return new PhoneField("(\\d{3}-\\d{3}-\\d{4})|None()", true);
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);

    }
  }
}
