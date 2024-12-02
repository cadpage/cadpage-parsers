package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMoniteauCountyParser extends FieldProgramParser {

  public MOMoniteauCountyParser() {
    this("MONITEAU COUNTY", "MO");
  }

  public MOMoniteauCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "Call:CALL! Sub:CALL/S! Place:PLACE! Address:ADDR! ( City:CITY! Apt:APT! | Apt:APT! City:CITY! State:ST! Zip:ZIP! ) Cross_Streets:X! " +
              "( Latitude:GPS1 Longitude:GPS2! | Map_Coords:GPS | MapCoordinates:GPS | ) Event#:ID! Reporting_Person:NAME? Phone#:PHONE? " +
              "( Unit:UNIT! Initiated:SKIP! ( Notes:INFO! | ProQANote:INFO! | ) INFO/N+ | ( Initiated:SKIP! | Initiated_Date:SKIP! Initiated_Time:SKIP! ) INFO/N+ Units%EMPTY UNIT/C+ ) ");
  }

  @Override
  public String getAliasCode() {
    return"MOMoniteauCounty";
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyStateField extends StateField {
    @Override
    public void doParse(String field, Data data) {
      if (field.equals("M")) field = "MO";
      super.doParse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Call Received on ")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile(".*\"([-+]?\\d{2,3}\\.\\d{3,})\".*\"([-+]?\\d{2,3}\\.\\d{3,})\".*");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }
    }
  }
}
