package net.anei.cadpage.parsers.FL;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLWaltonCountyParser extends FieldProgramParser {

  public FLWaltonCountyParser() {
    super("WALTON COUNTY", "FL",
          "REPORTED:SKIP! TIME:DATETIME! RUN_#:ID! CAD_CALL_#:ID/L! RESPONDING_UNITS:UNIT! DETERMINANT:CODE! DESC:CALL! NATURE:CALL! " +
          "ZONE:MAP! ADDRESS:ADDR! CITY:CITY! CROSS_STREETS:X! GPS! DIRECTIONS:INFO! COMMENTS:INFO! INFO/N+ CALLER:NAME! PHONE_#:PHONE! END");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;

    //  Sometimes they switch place and address fields
    if (!data.strPlace.isEmpty() &&
        checkAddress(data.strPlace) > checkAddress(data.strAddress)) {
          String tmp = data.strAddress;
          data.strAddress = data.strPlace;
          data.strPlace = tmp;
        }
    return true;
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyy");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("[A-Z][a-z]{2} [A-Z][a-z]{2} \\d+ \\d\\d:\\d\\d:\\d\\d \\d{4}", DATE_TIME_FMT, true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("LAT= *(\\S*) *LON= *(\\S*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+','+match.group(2), data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d [A-Z ]+:");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_HDR_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
