package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAGilmerCountyParser extends FieldProgramParser {

  public GAGilmerCountyParser() {
    super("GILMER COUNTY", "GA",
          "Street_Address:ADDR! City:CITY! Call_Type:CALL! Call_DateTime:DATETIME! Caller_Number:PHONE! Comment:INFO! LatLon:GPS! END");
  }

  @Override
  public String getFilter() {
    return "gilmer.ga@ez911map.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d+)(?:  .*)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    int pt = body.indexOf("\n>From :");
    if (pt < 0) return false;
    body = body.substring(0, pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.*?) */+ *(.*)");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPlace = match.group(2);
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +/+ +");

  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      data.strSupp = INFO_BRK_PTN.matcher(field).replaceAll("\n");
    }
  }
}
