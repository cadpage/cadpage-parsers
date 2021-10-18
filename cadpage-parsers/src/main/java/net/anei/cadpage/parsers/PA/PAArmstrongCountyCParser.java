package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAArmstrongCountyCParser extends FieldProgramParser {

  public PAArmstrongCountyCParser() {
    super(CITY_CODES, "ARMSTRONG COUNTY", "PA",
          "( INFO:CALL! LOCALE:ADDRCITY! ADD'L_LOCALE_INFO:INFO! LANDMARK:PLACE! INTER:X! CFS#:ID! " +
          "| CALL_NUMBER:ID? CALL_TYPE:CALL! ( LOCATION:ADDRCITY! TAC:CH! | TAC:CH LOCATION:ADDRCITY! ) CLOSEST_INTERSECTION:X! COMMON_NAME:PLACE! LAT/LONG:GPS! NARRATIVE:INFO " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern BAD_MSG_PTN = Pattern.compile("(.* LAT/LONG:[^:]*\\b(?:40|79))([^.].*)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Dispatch:")) return false;
    body = body.substring(9).trim();

    if (!body.contains("NARRATIVE:")) {
        Matcher match = BAD_MSG_PTN.matcher(body);
      if (match.matches()) {
        body = match.group(1) + " NARRATIVE:" + stripFieldStart(match.group(2), "=");
      }
    }
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Parser p =  new Parser(field);
      data.strCity = fixCity(p.getLastOptional(','));
      String city = p.getLastOptional('=');
      if (!city.isEmpty()) {
        data.strCity = append(city, ",", data.strCity);
        apt = stripFieldStart(p.getLastOptional('='), "APT");
      }
      field = p.get().replace('@',  '&');
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    private String fixCity(String city) {
      if (city.isEmpty()) return city;
      city = convertCodes(city, CITY_CODES);
      city = stripFieldEnd(city, " BORO");
      return city;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('~', ',');
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LEECHBURG BORO",    "LEECHBURG",
      "KISKI TWP",         "KISKIMINETAS TWP",
      "KITTG BORO",        "KITTANNING",
      "KITTG TWP",         "KITTANNING TWP"
  });

}
