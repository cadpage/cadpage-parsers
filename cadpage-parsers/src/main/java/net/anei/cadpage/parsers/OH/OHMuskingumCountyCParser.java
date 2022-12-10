package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHMuskingumCountyCParser extends DispatchH05Parser {

  public OHMuskingumCountyCParser() {
    super("MUSKINGUM COUNTY", "OH",
          "Address:ADDRCITY/S6! CALL_DATETIME_ID! GPS? Alert:ALERT? Cross_Streets:X! CFS#:SKIP ( Incident_number:ID2 | INCIDENT_NUMBER:ID2? ) Narrative:EMPTY! INFO_BLK+ Times:EMPTY TIMES+ Final_Report:SKIP");
  }

  @Override
  public String getFilter() {
    return "@ohiomuskingumsheriff.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL_DATETIME_ID")) return new MyCallDateTimeIdField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID2")) return new MyId2Field();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field, data);
      if (data.strApt.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, data.strApt);
      }
      String city = CITY_SET.getCode(data.strCity);
      if (city == null) {
        data.strPlace = data.strCity;
        data.strCity = "";
      } else {
        data.strPlace = data.strCity.substring(city.length()).trim();
        data.strCity = city;
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + "  PLACE";
    }
  }

  private static final Pattern CALL_DATETIME_ID_PTN = Pattern.compile("Call Type:(.*)Date/Time:(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)(?: *CFS#:(.*))?");
  private class MyCallDateTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_DATETIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall =  match.group(1).trim();
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strCallId = getOptGroup(match.group(4));
    }

    @Override
    public String getFieldNames() {
      return "CALL DATE TIME ID";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("Long:(.*) Lat:(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(2) + ',' + match.group(1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      StringBuilder sb = new StringBuilder();
      for (String part : field.split(",")) {
        part = part.trim();
        part = stripFieldStart(part, "[");
        part = stripFieldEnd(part, "]");
        if (part.startsWith("Incident not yet created")) continue;
        if (sb.length() > 0) sb.append(", ");
        sb.append(part);
      }
      if (sb.length() > 0) {
        if (data.strCallId.length() > 0) {
          sb.append(" / ");
          sb.append(data.strCallId);
        }
        data.strCallId = sb.toString();
      }
    }
  }

  private static CodeSet CITY_SET = new CodeSet(

      // Cities
      "ZANESVILLE",

      // Villages
      "ADAMSVILLE",
      "DRESDEN",
      "FRAZEYSBURG",
      "FULTONHAM",
      "GRATIOT",
      "NEW CONCORD",
      "NORWICH",
      "PHILO",
      "ROSEVILLE",
      "SOUTH ZANESVILLE",

      // Townships
      "ADAMS",
      "BLUE ROCK",
      "BRUSH CREEK",
      "CASS",
      "CLAY",
      "FALLS",
      "HARRISON",
      "HIGHLAND",
      "HOPEWELL",
      "JACKSON",
      "JEFFERSON",
      "LICKING",
      "MADISON",
      "MEIGS",
      "MONROE",
      "MUSKINGUM",
      "NEWTON",
      "PERRY",
      "RICH HILL",
      "SALEM",
      "SALT CREEK",
      "SPRINGFIELD",
      "UNION",
      "WASHINGTON",
      "WAYNE",

      // Census-designated places
      "DUNCAN FALLS",
      "EAST FULTONHAM",
      "NORTH ZANESVILLE",
      "PLEASANT GROVE",
      "TRINWAY",

      // Other unincorporated communities
      "ADAMS MILLS",
      "BLOOMFIELD",
      "BLUE ROCK",
      "BRIDGEVILLE",
      "CHANDLERSVILLE",
      "COAL HILL",
      "DILLON FALLS",
      "ELLIS",
      "FREELAND",
      "GAYSPORT",
      "GILBERT",
      "HIGH HILL",
      "HOPEWELL",
      "IRVILLE",
      "LICKING VIEW",
      "MATTINGLY SETTLEMENT",
      "MEADOW FARM",
      "MUSEVILLE",
      "NASHPORT",
      "OTSEGO",
      "RIX MILLS",
      "RURALDALE",
      "SONORA",
      "STOVERTOWN",
      "SUNDALE",
      "WHITE COTTAGE",
      "YOUNG HICKORY",
      "ZENO"
  );
}
