package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA77Parser extends FieldProgramParser {

  private String marker;
  private Properties cityCodes;

  public DispatchA77Parser(String marker, String defCity, String defState) {
    this(marker, null, defCity, defState);
  }

  public DispatchA77Parser(String marker, Properties cityCodes, String defCity, String defState) {
    super(defCity, defState,
          "CALL ADDRCITY! ( CALL/SDS Cross_Street:X ( GPS | GPS1 GPS2 ) END " +
                         "| Cross_Street:X ( GPS | GPS1 GPS2 ) END " +
                         "| GPS END " +
                         "| GPS1 GPS2 END " +
                         "| CALL/SDS EMPTY+? X/Z? ( GPS | GPS1 GPS2 ) END " +
                         ")");
    this.marker = marker;
    this.cityCodes = cityCodes;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(marker)) return false;
    return parseFields(body.split("\\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField(0);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern BOUND_PTN =  Pattern.compile("[NSEW]B", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN1 = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN2 = Pattern.compile("[A-Z]?\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);

  private class MyAddressCityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(':');
      if (cityCodes != null) city = convertCodes(city, cityCodes);
      data.strCity = city;

      String apt = "";
      String addrExt = "";
      while (true) {
        String place = p.getLastOptional(';');
        if (place.isEmpty()) break;
        Matcher match;
        if (BOUND_PTN.matcher(place).matches()) {
          addrExt = append(addrExt, " ", place);
        }
        else if ((match = APT_PTN1.matcher(place)).matches()) {
          apt = append(apt, "-", match.group(1));
        }
        else if (APT_PTN2.matcher(place).matches()) {
          apt = append(apt, "-", place);
        } else {
          data.strPlace = append(place, " - ", data.strPlace);
        }
      }
      parseAddress(p.get(), data);
      data.strAddress = append(data.strAddress, " ", addrExt);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }

  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);;
    }
  }

  private static final String GPS_PTN_STR = "(?:[-+]?\\d{2,3}\\.\\d{6,}|-361)";
  private static final Pattern GPS_PTN1 = Pattern.compile(GPS_PTN_STR+','+GPS_PTN_STR);
  private static final Pattern GPS_PTN2 = Pattern.compile(GPS_PTN_STR);

  private class MyGPSField extends GPSField {
    MyGPSField(int type) {
      super(type);
      setPattern(type == 0 ? GPS_PTN1 : GPS_PTN2, true);
    }
  }
}
