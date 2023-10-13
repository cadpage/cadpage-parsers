
package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Marion County, AL
 */
public class ALMarionCountyParser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile(">>> ?MARION COUNTY 911 (?:NOTIFICATION|UPDATE) ?<<< +={20,} +");

  public ALMarionCountyParser() {
    super(CITY_LIST, "MARION COUNTY", "AL",
          "Unit:UNIT? Event:ID! Location:ADDR/S! LAT/LONG:GPS? Event_Type:CALL! Latitude:GPS1? Longitude:GPS2? Event_SubType:CALL/SDS! Complainant_Name:NAME! Complainant_Phone:PHONE INFO+");
  }

  @Override
  public String getFilter() {
    return "911call@marion911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();

    int pt = body.indexOf("  ----------");
    if (pt >= 0) body = body.substring(0,pt);

    body = body.replace(" Changed to:", ":");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern APT_PTN1 = Pattern.compile("(?:APT|LOT|ROOM|RM|STE)[# ]*(.*)");
  private static final Pattern APT_PTN2 = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      String city = null;
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        if (STATE_PTN.matcher(city).matches()) {
          field = field.substring(0, pt).trim();
          data.strState = city;
          city = null;
        }
      }

      if (city != null) {
        if (city.length() > 0 && Character.isAlphabetic(city.charAt(0))) {
          field = field.substring(0, pt).trim();
          pt = city.indexOf("  ");
          if (pt >= 0) {
            data.strCity = city.substring(0,pt);
          } else if (city.contains(" ")) {
            parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
            if (data.strCity.isEmpty()) data.strCity = city;
          } else {
            data.strCity = city;
          }
        }
      } else {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
        field = getStart();
        field = stripFieldEnd(field, " " + data.strCity);
      }


      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      for (String part : field.split("[:@]")) {
        part = part.trim();
        if (part.isEmpty() || part.equals("@") || part.equals("Nearest")) continue;

        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
          continue;
        }

        if (GPS_PATTERN.matcher(part).matches()) {
          setGPSLoc(part, data);
          continue;
        }

        Matcher match = APT_PTN1.matcher(part);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
          continue;
        }

        if (APT_PTN2.matcher(part).matches()) {
          data.strApt = append(data.strApt, "-", part);
          continue;
        }

        data.strPlace = append(data.strPlace, " - ", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT GPS? CITY ST";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "BYRD",           "HAMILTON",
      "PEA RIDGE",      "BRILLIANT",
      "SHILOH",         "HAMILTON",
  });

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "GUIN",
    "HALEYVILLE",
    "HAMILTON",
    "WINFIELD",

    // Towns
    "BEAR CREEK",
    "BRILLIANT",
    "GLEN ALLEN",
    "GU-WIN",
    "HACKLEBURG",
    "TWIN",
    "YAMPERTOWN",

    // Unincorporated communities
    "BARNESVILLE",
    "BEXAR",
    "PIGEYE",
    "PULL TIGHT",
    "SOUTH HALEYVILLE",

    // Ghost town
    "PIKEVILLE",

    //missed places
    "SHOTTSVILLE",
    "SHILOH",
    "BYRD",
    "PEA RIDGE",

    // Lamar County
    "SULLIGENT"
  };
}
