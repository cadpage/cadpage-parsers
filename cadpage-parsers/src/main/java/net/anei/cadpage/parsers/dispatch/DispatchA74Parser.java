package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA74Parser extends FieldProgramParser {

  public static final int FLG_LEAD_PLACE = 1;

  private boolean leadPlace;

  public DispatchA74Parser(String defCity, String defState) {
    this(null, defCity, defState, 0);
  }

  public DispatchA74Parser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, 0);
  }

  public DispatchA74Parser(String[] cityList, String defCity, String defState, int flags) {
    super(cityList, defCity, defState,
          "( ID1 CALL! ADDRCITY " +
          "| CALL ADDRCITY ID2 " +
          ") INFO/N+");
    leadPlace = (flags & FLG_LEAD_PLACE) != 0;
  }

  @Override
  public String getFilter() {
    return "dispatch";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD DISPATCH") && !subject.equals("CAD INCIDENT")) return false;
    body = stripFieldStart(body,  "1/1:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("CAD #((?:\\d{2,10}-)?\\d+):", true);
    if (name.equals("ID2")) return new IdField("\\d{9}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("(.*?) *\\(([-+]?[.0-9]+, *[-+]?[.0-9]+)\\)");
  private static final Pattern ADDR_CITY_UNIT_PTN = Pattern.compile(" *[\\(\\[](.*?)[\\]\\)] *");
  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("(.*?)(?: (?:APT|RM|LOT) +(.*?))?(?: *\\[(.*?)\\])?");
  private static final Pattern COMMA_PTN = Pattern.compile(" *, *");
  private static final Pattern CITY_PTN = Pattern.compile("[A-Z ]*");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      // Strip off trailing GPS
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        setGPSLoc(match.group(2), data);
      }

      // Intersections have their own protocol
      if (field.startsWith("INTERSECTION:")) {
        field = field.substring(13).trim();

        // Intersections can have a city or city/unit combination in round or square brackets
        // The construct usually appears twice but we will assume the city/unit information is duplicated
        match = ADDR_CITY_UNIT_PTN.matcher(field);
        if (match.find()) {
          StringBuffer sb = new StringBuffer();
          do {
            String term = match.group(1);
            String city, unit;
            int pt = term.lastIndexOf('/');
            if (pt >= 0) {
              city = term.substring(0,pt).trim();
              unit = term.substring(pt+1).trim();
            } else if (CITY_PTN.matcher(term).matches()) {
              city = term;
              unit = null;
            } else {
              city = null;
              unit = term;
            }

            if (city != null && data.strCity.isEmpty()) data.strCity = city;
            if (unit != null && !data.strUnit.equals(unit)) data.strUnit = append(data.strUnit, ",", unit);

            match.appendReplacement(sb, match.hitEnd() ? "" : " ");
          } while (match.find());
          match.appendTail(sb);
          field = sb.toString();
        }
        parseAddress(field, data);
      }

      // Regular non-intersection processing
      else {
        match = ADDR_UNIT_PTN.matcher(field);
        String apt = null;
        if (match.matches()) {
          field = match.group(1).trim();
          apt = match.group(2);
          data.strUnit = getOptGroup(match.group(3));
        }
        String[] parts = COMMA_PTN.split(field);
        switch (parts.length) {
        case 1:
          field = stripFieldEnd(parts[0], ' ' + data.defState);
          field = field.replace('@', '&');
          StartType st = leadPlace ? StartType.START_PLACE : StartType.START_ADDR;
          parseAddress(st, FLAG_ANCHOR_END, field, data);
          if (data.strAddress.isEmpty()) {
            parseAddress(data.strPlace, data);
            data.strPlace = "";
          }
          break;

        case 2:
          if (CITY_PTN.matcher(parts[1]).matches()) {
            parseAddress(parts[0], data);
            data.strCity = parts[1];
          } else {
            data.strPlace = parts[0];
            parseAddress(parts[1], data);
          }
          break;

        case 3:
          data.strPlace = parts[0];
          parseAddress(parts[1], data);
          data.strCity = parts[2];
          break;

        default:
          abort();
        }

        if (apt != null) data.strApt = append(data.strApt, "-", apt.trim());
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " UNIT GPS";
    }
  }

  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +([-A-Z0-9]+)\\b[ .;]*(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("URL:")) {
        field = field.substring(4).trim();
        if (!field.contains("www.google.com")) data.strInfoURL = field;
        return;
      }

      Matcher match = INFO_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT " + super.getFieldNames();
    }
  }
}
