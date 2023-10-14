package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;

public class COParkCountyAParser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile("(?:\\(CAD\\) +|CAD[ /]+)");
  private static final Pattern PLACE_PTN = Pattern.compile("(.*)\\((.*)\\)(.*)");

  public COParkCountyAParser() {
    super(CITY_LIST, "PARK COUNTY", "CO",
          "Call_Number:ADDR/SC! Map_Link:GPS! Caller:NAME! Description:INFO! END");
  }

  @Override
  public String getFilter() {
    return "parkcodispatch@parkco.us,777";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // There should be a marker, but very occasionally there is not
    body = stripFieldStart(body, "Park County Government:");
    do {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end());
        break;
      }

      if (subject.equals("CAD")) break;

      if (isPositiveId()) break;

      return false;
    } while (false);

    int pt = body.indexOf("\nText STOP");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // New format starts with Call Number: label
    if (body.startsWith("Call Number:")) {
      return parseMsg(body, data);
    }

    // Otherwise process old format
    else {
      setFieldList("ADDR APT PLACE CITY CALL");

      // We have a couple tricks.  First look for a place name in parens.  If found
      // it also marks the beginning of the call description
      Matcher match = PLACE_PTN.matcher(body);
      if (match.matches()) {
        body = match.group(1).trim();
        data.strPlace = match.group(2).trim();
        data.strCall = match.group(3).trim();
      }

      // No luck there, see if we can identify the call description
      // at the end of the message  (It doesn't always have a space delimiter :(
      else {
        String call = CALL_LIST.getCode(body);
        if (call != null) {
          data.strCall = call;
          body = body.substring(0,body.length()-call.length()).trim();
        }
      }

      // Either way, having identified the call description makes life easier
      if (data.strCall.length() > 0) {

        // Then see if we can parse a city name from the end of the line
        // if that fails, parse an address and save the rest in place
        Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, body);
        if (res.getCity().length() > 0) {
          res.getData(data);
        } else {
          parseAddress(StartType.START_ADDR, body, data);
          data.strPlace = getLeft();
        }
      }

      else {
        // No such luck.  Just hope the address parser can make sense of this
        parseAddress(StartType.START_ADDR, body, data);
        data.strCall = getLeft();
        if (data.strCall.length() == 0) return false;
      }

      return true;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ID_ADDR_PTN = Pattern.compile("(\\d+) +(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      field = match.group(2);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID " + super.getFieldNames();
    }
  }

  private static final Pattern INFO_TRAIL_JUNK = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d .*\\]$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        line = INFO_TRAIL_JUNK.matcher(line).replaceFirst("");
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  @Override
  protected boolean isHouseNumber(String token) {

    // Mile  markers can be treated as house numbers
    if (MM_PTN.matcher(token).matches()) return true;
    return super.isHouseNumber(token);
  }
  private static final Pattern MM_PTN = Pattern.compile("MM\\d+");


  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private CodeSet CALL_LIST = new ReverseCodeSet(
      "ACCIDENT/ CRASH",
      "ACCIDENT/CRASH",
      "ACCIDETN/CRASH",
      "ACCIDENT/MOTOR VEHICLE",
      "ALARM CARBON MONOXIDE",
      "ALARM FIRE",
      "ALARM SMOKE",
      "FIRE ILLEGAL CAMPFIRE",
      "FIRE MISC",
      "FIRE STRUCTURE",
      "GAS ODOR/LEAK",
      "MEDICAL",
      "MEDICAL/TRAUMA",
      "SMOKE REPORT",
      "VEHICLE SLIDE OFF",
      "FIRE WILDLAND"
 );

  private static final String[] CITY_LIST = new String[]{

    // Towns
    "ALMA",
    "FAIRPLAY",

    // Census-designated place
    "GUFFEY",

    //Unincorporated communities
    "BAILEY",
    "COMO",
    "GRANT",
    "HARTSEL",
    "JEFFERSON",
    "LAKE GEORGE",
    "SHAWNEE",
    "TARRYALL",

    // Ghost Towns
    "ANTERO JUNCTION",
    "BUCKSKIN JOE",
    "GARO",
    "HOWBERT",
    "LAURET",
    "LAURETTE",
    "TARRYALL",

    // Alma Subdivisions
    "ALMA PARK ESTATES",

    // Bailey Subdivisions
    "BAILEY ESTATES",
    "BAILEY VIEW",
    "BURLAND RANCH-ETTES",
    "DEER CREEK VALLEY RANCHOS",
    "DOUBLE S RANCHETTES",
    "ELK CREEK HIGHLANDS",
    "ELK CREEK MEADOWS",
    "HARRIS PARK ESTATES",
    "HORSESHOE PARK",
    "K-Z RANCH ESTATES",
    "PARKVIEW",
    "ROLAND VALLEY",
    "TRAILS WEST",
    "WILL-O-WISP",

    // Fairplay Subdivisions
    "BREAKNECK PASS RANCH AMEND",
    "FAIRPLAY CLARK AND BOGUES",
    "FOURMILE FISHING CLUB",
    "FRIENDSHIP RANCH",
    "WARM SPRINGS",
    "VALLEY OF THE SUN",

    // Florissant Subdivisions
    "BADGER CREEK RANCH",
    "ECHO VALLEY ESTATES",
    "RAVENSWOOD",

    // Hartsel Subdivisions
    "WESTERN UNION RANCH",

    // Jefferson Subdivisions
    "CIRCLE R RANCH",
    "ELKHORN RANCHES",
    "INDINA MOUNTAIN",   // Misspelled
    "INDIAN MOUNTAIN",
    "MICHIGAN HILL",
    "STAGESTOP",

    // Lake George Subdivisions
    "WILDWOOD REC VILLAGE",

    // Pine Subdivisions
    "WOODSIDE PARK",

    // Counties
    "CHAFEE COUNTY",
    "CLEAR CREEK COUNTY",
    "FREMONT COUNTY",
    "JEFFERSON COUNTY",
    "LAKE COUNTY",
    "PARK COUNTY",
    "SUMMIT COUNTY",
    "TELLER COUNTY"
  };

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ALMA PARK ESTATES",         "ALMA",

      "BAILEY ESTATES",            "BAILEY",
      "BAILEY VIEW",               "BAILEY",
      "BURLAND RANCH-ETTES",       "BAILEY",
      "DEER CREEK VALLEY RANCHOS", "BAILEY",
      "DOUBLE S RANCHETTES",       "BAILEY",
      "ELK CREEK HIGHLANDS",       "BAILEY",
      "ELK CREEK MEADOWS",         "BAILEY",     // Possibly FAIRPLAY
      "HARRIS PARK ESTATES",       "BAILEY",
      "HORSESHOE PARK",            "BAILEY",
      "K-Z RANCH ESTATES",         "BAILEY",
      "PARKVIEW",                  "BAILEY",
      "ROLAND VALLEY",             "BAILEY",
      "TRAILS WEST",               "BAILEY",
      "WILL-O-WISP",               "BAILEY",

      "BREAKNECK PASS RANCH AMEND", "FAIRPLAY",  // ???
      "FAIRPLAY CLARK AND BOGUES", "FAIRPLAY",   // Possibly BAILEY
      "FOURMILE FISHING CLUB",     "FAIRPLAY",
      "FRIENDSHIP RANCH",          "FAIRPLAY",
      "WARM SPRINGS",              "FAIRPLAY",
      "VALLEY OF THE SUN",         "FAIRPLAY",

      "BADGER CREEK RANCH",        "FLORISSANT", // Possibly GUFFEY
      "ECHO VALLEY ESTATES",       "FLORISSANT",
      "RAVENSWOOD",                "FLORISSANT",

      "WESTERN UNION RANCH",       "HARTSEL",

      "CIRCLE R RANCH",            "JEFFERSON",
      "ELKHORN RANCHES",           "JEFFERSON",
      "INDINA MOUNTAIN",           "JEFFERSON",
      "INDIAN MOUNTAIN",           "JEFFERSON",
      "MICHIGAN HILL",             "JEFFERSON",
      "STAGESTOP",                 "JEFFERSON",

      "WILDWOOD REC VILLAGE",      "LAKE GEORGE",  // ???

      "WOODSIDE PARK",             "PINE"
  });
}
