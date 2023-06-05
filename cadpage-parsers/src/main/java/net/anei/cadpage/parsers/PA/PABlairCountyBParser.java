package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PABlairCountyBParser extends FieldProgramParser {

  public PABlairCountyBParser() {
    super("BLAIR COUNTY", "PA",
          "Units:UNIT! Inc_Code:CALL! Address:ADDRCITY/S6! X-ST:X! CFS_Number:ID! Date/Time:DATETIME! Narrative:INFO/N+ Lat/Lon:GPS");
  }

  @Override
  public String getFilter() {
    return "blair911@atlanticbbn.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private Pattern ADDR_MAP_PTN = Pattern.compile("(.*) ((?:[NSEW]|[NS][EW]) SECTOR)");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      int pt = field.lastIndexOf(',');
      if (!field.startsWith("LAT:")) {
        if (pt >= 0) {
          String city = field.substring(pt+1).trim();
          city = stripFieldEnd(city, " BORO");
          field = field.substring(0,pt).trim();

          pt = city.indexOf('-');
          if (pt >= 0) city = city.substring(pt+1).trim();
          data.strCity = city;
        }
      }
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT MAP CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "ProQA Paramount Medical:");
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
