package net.anei.cadpage.parsers.WY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class WYAlbanyCountyParser extends DispatchH05Parser {

  public WYAlbanyCountyParser() {
    super("ALBANY COUNTY", "WY",
          "( CALL:CALL1! PLACE:PLACE! ADDR:ADDRCITY1! City:CITY1! CROSS:X! ID:ID! PRI:PRI! DATE:DATETIME1! MAP:MAP! " +
                "Latitude:GPS1! Longitude:GPS2! UNIT:UNIT! INFO:EMPTY! NOTES:EMPTY! INFO/N+ https:SKIP " +
          "| Call_Date/Time:EMPTY! Call_Address:ADDRCITY2! PLACE2 X2 GPS Narrative:INFO! INFO/N+? CALL2! CALL/SDS " +
          ")");
  }

  @Override
  public String getFilter() {
    return "LARCMail@cityoflaramie.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("CITY1")) return new MyCity1Field();
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("PLACE2")) return new PlaceField("Common Name *(.*)", true);
    if (name.equals("X2")) return new CrossField("Cross Streets *(.*)", true);
    if (name.equals("CALL2")) return new CallField("Nature of Call *(.*)", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }

  private static final Pattern INTERNAL_CR_PTN = Pattern.compile("\\bCR \\d+ (AV|LN|RD|ST)\\b");

  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String city = "";
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      field = field.replace('@', '&');
      field = INTERNAL_CR_PTN.matcher(field).replaceAll("$1");
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (!data.strApt.isEmpty()) {
        city = stripFieldEnd(city, ' ' + data.strApt);
      }
      data.strCity = city;
    }
  }

  private class MyCity1Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCity.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private class MyAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("Qualifier"), data);
      data.strApt = append(data.strApt, "-", p.get());
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("Lat +(\\S+) +Long +(\\S+)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+','+match.group(2), data);
    }
  }
}
