package net.anei.cadpage.parsers.MO;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MODunklinCountyParser extends SmartAddressParser {

  public MODunklinCountyParser() {
    super(CITY_LIST, "DUNKLIN COUNTY", "MO");
    setFieldList("CALL ADDR APT CITY ST PLACE INFO NAME PHONE GPS UNIT");
  }

  @Override
  public String getFilter() {
    return "dunklin911@gmail.com";
  }

  private static final Pattern MISSING_SPC_PTN = Pattern.compile("(?<=\\b\\d{3}-\\d{4})(?=[A-Z0-9]\\S* - )");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile(" +(\\S+) - (?:Dispatch|Enroute|On Scene) ");
  private static final Pattern TRAIL_PHONE_PTN = Pattern.compile(" +(\\(\\d{3}\\) \\d{3}-\\d{4})$");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +Number (\\(\\d{3}\\) \\d{3}-\\d{4}); Lat ([-+]?[.0-9]+); Lon ([-+]?[.0-9]+); ESN .*$");
  private static final Pattern TRAIL_NAME_PTN = Pattern.compile(" +(?!None)([A-Z][A-Za-z]+(?:,? [A-Z][A-Za-z]+)?)$");
  private static final Pattern INFO_DELIM_PTN = Pattern.compile(";? \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    // Add missing space delimiter behind any phone number
    body = MISSING_SPC_PTN.matcher(body).replaceAll(" ");

    // Extract unit information from times info
    Matcher match = TRAIL_UNIT_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0, match.start());
      Set<String> unitSet = new HashSet<>();
      do {
        String unit = match.group(1);
        if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
      } while (match.find());
    }

    // Extract trailing phone number
    match = TRAIL_PHONE_PTN.matcher(body);
    if (match.find()) {
      data.strPhone = match.group(1);
      body = body.substring(0, match.start());
    } else {
      match = TRAIL_GPS_PTN.matcher(body);
      if (match.find()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
        body = body.substring(0, match.start());
      }
    }

    // And trailing name
    if (body.endsWith(" None")) {
      body = body.substring(0,body.length()-5).trim();
    } else {
      match = TRAIL_NAME_PTN.matcher(body);
      if (match.find()) {
        data.strName = match.group(1);
        body = body.substring(0, match.start());
      }
    }

    // And trailing info block
    if (body.endsWith(" None")) {
      body = body.substring(0, body.length()-5);
    } else {
      String[] flds = INFO_DELIM_PTN.split(body);
      body = flds[0].trim();
      for (int ndx = 1; ndx < flds.length; ndx++) {
        data.strSupp = append(data.strSupp, "\n", flds[ndx].trim());
      }
    }

    // Next comes a trailing place block.
    // Things get much easier if there isn't one

    if (body.endsWith(" None")) {
      body = body.substring(0, body.length()-5).trim();
      Parser p = new Parser(body);
      String city = p.getLastOptional(',');
      match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      parseAddress(p.get(), data);
      return true;
    }

    // Otherwise things get complicated

    else {

      // See if we have a comma separated
      int pt1 = body.indexOf(',');
      if (pt1 >= 0) {

        // OK, that identifies the address
        // check for a second comma delimiting the state
        parseAddress(body.substring(0,pt1), data);
        String city = body.substring(pt1+1).trim();
        pt1 = city.indexOf(',');
        if (pt1 >= 0) {
          String state = city.substring(pt1+1).trim();
          match = ST_ZIP_PTN.matcher(state);
          if (match.lookingAt()) {
            data.strCity = city.substring(0, pt1).trim();
            data.strState = match.group(1);
            data.strPlace = state.substring(match.end()).trim();
            return true;
          }

          // No such luck.  See if the first comma delimits the state field
          match = ST_ZIP_PTN.matcher(city);
          if (match.lookingAt()) {
            data.strCity = match.group(2);
            data.strState = match.group(1);
            data.strPlace = city.substring(match.end()).trim();
            return true;
          }
        }

        // See if we can identify a city after the first comma
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (!data.strCity.isEmpty()) {
          data.strPlace = getLeft();
          return true;
        }
      }

      // If nothing else has worked, see what the smart address parser can make of this
      parseAddress(StartType.START_ADDR, 0, body, data);
      String left = getLeft();
      if (data.strAddress.isEmpty()) {
        parseAddress(body, data);
      } else {
        data.strPlace = left;
      }
      return true;
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT")) city = "";
    return city;
  }

  private static final String[] CITY_LIST = new String[] {
      "DUNKLIN COUNTY",
      "DUNKLIN CO",
      "OUT",

      "ARBYRD",
      "ARKMO",
      "BAIRD",
      "BRIAN",
      "BUCK DONIC",
      "BUCODA",
      "CAMPBELL",
      "CARDWELL",
      "CARUTH",
      "CLARKTON",
      "COCKRUM",
      "COTTON PLANT",
      "DILLMAN",
      "EUROPA",
      "FRISBEE",
      "GIBSON",
      "GLENNONVILLE",
      "GOBLER",
      "HARGROVE",
      "HOLCOMB",
      "HOLLYWOOD",
      "HORNERSVILLE",
      "IPLEY",
      "KENNETT",
      "MACKEYS",
      "MALDEN",
      "MARLOW",
      "MCGUIRE",
      "NESBIT",
      "OCTA",
      "PROVIDENCE",
      "RIVES",
      "SENATH",
      "SUMACH",
      "TOWNLEY",
      "VALLEY RIDGE",
      "VINCIT",
      "WHITE OAK",
      "WILHELMINA",
      "WRIGHTSVILLE"

  };
}
