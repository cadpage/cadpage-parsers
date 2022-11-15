package net.anei.cadpage.parsers.FL;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class FLOkaloosaCountyAParser extends FieldProgramParser {

  public FLOkaloosaCountyAParser() {
    super(CITY_CODES, "OKALOOSA COUNTY", "FL",
          "Incident:ID Run_Zone:MAP Complaint:CALL Unit:UNIT " +
             "( Station:SRC! Complaint:CALL! Address:ADDR/S6! Cross_Street:X! Place:PLACE! " +
             "| Address:ADDR/S6! Cross_Street:X Place:PLACE " +
             "| Location:ADDR? Place:PLACE! Apt/Lot:APT! Run_Zone:MAP! City:CITY! Cross_Street1:X! Cross_Street2:X! Complaint:CALL? " +
             ") Map:GPS Units:TIMES/N+");
    setupCities(CITY_LIST);
    setupSpecialStreets("CALLE DE TALENCIA");
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MARKER = Pattern.compile("(?:ECC|[a-z]+): *");
  private Set<String> unitSet = new HashSet<String>();

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    if (body.startsWith("ncident:")) {
      body = 'I' + body;
    } else if (body.startsWith("cident:")) {
      body = "In" + body;
    } else if (body.startsWith("IIncident:")) {
      body = body.substring(1);
    } else if (body.startsWith("omplaint:")) {
      body = 'C' + body;
    }

    unitSet.clear();
    try {
      if (parseFields(body.split("\n"), data)) return true;
    } finally {
      unitSet.clear();
    }

    // If that did not work, try looking for a generic dispatch alert
    if (body.contains("\n"))  return false;
    setFieldList("CALL ADDR APT CITY INFO");
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
    data.strSupp = getLeft();
    return isValidAddress();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID"))  return new IdField("[A-Z]{4}\\d{2}CAD\\d{6}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CROSS")) return new MyCrossField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      for (String unit : field.split(" +")) {
        unitSet.add(unit);
      }
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("SCENIC HIGHWAY", "HIGHWAY");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("SCENIC HIGHWAY", "HIGHWAY");
      super.parse(field, data);
    }
  }

  private static final Pattern PLACE_STREET_PREFIX_PTN = Pattern.compile("\\d+[A-Z]? ");
  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("(.*?) +(\\d{3}[- ](?:\\d{3}[- ])?\\d{4})");
  private static final Pattern PLACE_APT_PTN = Pattern.compile(" UNIT +(\\S+)\\b");
  private static final Pattern PLACE_ST_ZIP_PTN = Pattern.compile("(.*) FL +(\\d{5})");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {

      // If this looks like it duplicates the address, just skip it
      Matcher match = PLACE_STREET_PREFIX_PTN.matcher(field);
      if (match.lookingAt()) {
        if (data.strAddress.startsWith(match.group())) {

          // But before we drop it, see if it contains any apt information
          match = PLACE_APT_PTN.matcher(field);
          if (match.find()) {
            String apt = match.group(1);
            if (!apt.equals(data.strApt)) {
              data.strApt = append(data.strApt, "-", apt);
            }
          }

          // And sometimes the place version of the address will contains a city name
          // that the original address does not.
          if (data.strCity.length() == 0) {
            match = PLACE_ST_ZIP_PTN.matcher(field);
            if (match.matches()) {
              Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim());
              data.strCity = res.getCity();
              if (data.strCity.length() == 0) data.strCity = match.group(2);
            }
          }
        }
        return;
      }

      match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE PHONE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("https://maps.google.com/\\?q=(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1).trim(), data);
      }
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) +(\\S+) +(\\S+) +([A-Z]+)");
  private class MyTimesField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) {
        if (data.strDate.length() == 0) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
        }
        String unit = match.group(3);
        if (unitSet.add(unit)) data.strUnit = append(data.strUnit, " ", unit);
        String status = match.group(4);
        if (status.equals("CANCELLED") || status.equals("REPORTING") || status.equals("ONSCENE")) {
          data.strSupp = append(data.strSupp, "\n", unit + ' ' + status);
        }
      }

    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT INFO";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("SCENIC HIGHWAY 98", "HWY 98");
    return super.adjustMapAddress(addr);
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CRESTVIEW",
      "DESTIN",
      "FORT WALTON BEACH",
      "LAUREL HILL",
      "MARY ESTHER",
      "NICEVILLE",
      "VALPARAISO",

      // Towns
      "CINCO BAYOU",
      "SHALIMAR",

      // Census-designated places
      "EGLIN AFB",
      "LAKE LORRAINE",
      "OCEAN CITY",
      "WRIGHT",

      // Other unincorporated communities
      "BAKER",
      "BLACKMAN",
      "BLUEWATER BAY",
      "CAMPTON",
      "DEERLAND",
      "DORCAS",
      "ESCAMBIA FARMS",
      "FLOROSA",
      "GARDEN CITY",
      "HOLT",
      "MILLIGAN",
      "OKALOOSA ISLAND",
      "SEMINOLE",
      "SVEA",
      "TIMPOOCHEE",
      "VILLA TASSO",
      "WYNNEHAVEN BEACH"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FT WALTON BCH",    "FORT WALTON BEACH"
  });
}
