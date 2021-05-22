package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class DispatchA43Parser extends FieldProgramParser {

  private final ReverseCodeSet citySet;

  public DispatchA43Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA43Parser(String[] cityList, String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! PLACE:PLACE? ADDR:ADDRCITY/S9Xa? CITY:CITY? ID:ID! PRI:PRI? INFO:INFO/N+ GPS:GPS?");
    citySet = (cityList == null ? null : new ReverseCodeSet(cityList));
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("GPS")) return new BaseGPSField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("Lat \\(([-+]?\\d+\\.\\d+)\\) Lon \\(([-+]?\\d+\\.\\d+)\\) *(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("[")) {
        int pt = field.lastIndexOf(']');
        if (pt >= 0) {
          String place = field.substring(1,pt).trim();
          field = field.substring(pt+1).trim();
          if (!place.equalsIgnoreCase(data.strPlace)) {
            data.strPlace = append(data.strPlace, " - ", place);
          }
        }
      }

      // There is not always a space between the address field and the trailing city, so we do our out
      // city extraction rather that counting on the smart parser logic
      String city = null;
      if (citySet != null) {
        city = citySet.getCode(field.toUpperCase());
        if (city != null) {
          int pt = field.length() - city.length();
          if (pt == 0) {
            city = null;
          } else {
            city = field.substring(pt);
            field = field.substring(0,pt).trim();
          }
        }
      }

      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = match.group(3);
      }

      field = field.replace('@', '/');
      super.parse(field, data);
      if (city != null) data.strCity = city;
      if (data.strApt.equals("None")) data.strApt = "";
    }

    @Override
    public String getFieldNames() {
      return "PLACE? GPS? " + super.getFieldNames();
    }
  }

  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);

      // Strip trailing city name from address and apt fields
      if (field.length() > 0) {
        data.strAddress = stripTrailCity(data.strAddress, field);
        data.strApt = stripTrailCity(data.strApt, field);
      }
    }

    private String stripTrailCity(String field, String city) {
      if (field.toUpperCase().endsWith(city.toUpperCase())) {
        field = field.substring(0, field.length()-city.length()).trim();
      }
      return field;
    }
  }

  private class BaseGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Latitude ");
      field = field.replace(" Longitude ", ",");
      super.parse(field, data);
    }
  }
}
