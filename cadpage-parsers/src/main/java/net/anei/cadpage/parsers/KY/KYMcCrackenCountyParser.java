package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KYMcCrackenCountyParser extends DispatchH05Parser {

  public KYMcCrackenCountyParser() {
    super("MCCRACKEN COUNTY", "KY",
          "CALL_DATE/TIME:DATETIME! CALL_TYPE:CALL! LOCATION:ADDRCITY! XST:X! CMTS:EMPTY! INFO_BLK+ UNITS:UNIT! INC#:ID! CALL_TIMES:EMPTY! TIMES+? GPS! END");
  }

  @Override
  public String getFilter() {
    return "cad@paducahky.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern APT_PLACE_PTN = Pattern.compile("(\\d+) *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String city = CITY_SET.getCode(data.strCity);
      if (city != null) {
        String extra = data.strCity.substring(city.length()).trim();
        data.strCity = city;
        if (!extra.isEmpty()) {
          if (extra.contains("/")) {
            data.strCross = extra;
          } else {
            Matcher match = APT_PLACE_PTN.matcher(extra);
            if (match.matches()) {
              data.strApt = append(data.strApt, "-", match.group(1));
              extra = match.group(2);
            }
            data.strPlace = extra;
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE X";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("LATITUDE: *(\\S*) +LONGITUDE: *(\\S*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  @Override
  protected boolean isCity(String city) {
    String tmp = CITY_SET.getCode(city);
    return tmp != null && tmp.equals(city);
  }

  private static final CodeSet CITY_SET = new CodeSet(

      // City
      "PADUCAH",

      // Census-designated places
      "FARLEY",
      "HENDRON",
      "MASSAC",
      "REIDLAND",

      /// Unincorporated communities
      "CAMELIA",
      "CECIL",
      "CIMOTA CITY",
      "FREEMONT",
      "FUTURE CITY",
      "GRAHAMVILLE",
      "HARDMONEY",
      "HEATH",
      "HOVEKAMP",
      "KREBS",
      "LONE OAK",
      "MAXON",
      "MELBER",
      "RAGLAND",
      "ROSSINGTON",
      "RUDOLPH",
      "SAINT JOHNS",
      "SHEEHAN BRIDGE",
      "WEST PADUCAH",

      // Graves County
      "BOAZ",
      "SYMSONIA",

      // Livingston County
      "LEDBETTER"
  );
}
