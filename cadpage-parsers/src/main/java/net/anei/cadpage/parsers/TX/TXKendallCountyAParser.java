package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class TXKendallCountyAParser extends DispatchA37Parser {

  public TXKendallCountyAParser() {
    super("Boerne Dispatch", CITY_CODES, "KENDALL COUNTY", "TX");
    addExtendedDirections();
    addRoadSuffixTerms("R");
    setupMultiWordStreets("SAN ANTONIO",
                          "RIDGE VIEW",
                          "RIVER BEND",
                          "ROLLING ACRES",
                          "SADDLE MOUNTAIN",
                          "RANCH CIRCLE",
                          "LOS INDIOS",
                          "LOS INDIOS RANCH",
                          "DELAWARE CREEK",
                          "SHADOW VALLEY",
                          "OAK PARK",
                          "DEER LAKE",
                          "OAK KNOLL",
                          "RANGER CREEK",
                          "SCENIC LOOP",
                          "FALCON POINT",
                          "VALLEY VIEW",
                          "WOODLAND RANCH",
                          "WARING WELFARE",
                          "SAGE OAKS",
                          "SHADOW KNOLL",
                          "ROLLING VIEW",
                          "FALL SPRINGS",
                          "BLUE RIDGE",
                          "OLD CURRY CREED",
                          "COUNTRY MEADOW",
                          "KENDALL WOODS",
                          "RIO CORDILLERA",
                          "RIO COLORADO",
                          "GUADALUPE BEND",
                          "SPARKLING SPRINGS",
                          "HIDDEN HAVEN",
                          "RATTLESNAKE BLUFF",
                          "STEEL VALLEY",
                          "WILD TURKEY",
                          "LIVE OAK",
                          "LAKE VIEW",
                          "COLD RIVER",
                          "DEER CREEK",
                          "CLEAR SKY",
                          "MENGER SPRINGS");
  }

  @Override
  public String getFilter() {
    return "joep@gvtc.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("BoerneDispatch:")) {
      body = body.substring(0,6)+' '+body.substring(6);
    }
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) {
      data.strCall = data.strSupp;
      data.strSupp = "";
    }
    return true;
  }

  @Override
  protected boolean parseMessageField(String field, Data data) {

    // Look for a normal address, Do not check for a city here, because
    // city names are commonly found in the information following the address
    Result res = parseAddress(StartType.START_ADDR, FLAG_NO_CITY | FLAG_NO_IMPLIED_APT, field);
    if (res.isValid()) {
      res.getData(data);
      field = res.getLeft();

      // See if we can find a valid city/st/zip combination, that markes a
      // pretty definitive city
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        String city = CITY_CODES.getProperty(match.group(2).toUpperCase());
        if (city != null) {
          String place = match.group(1);
          data.strCity = city;
          field = match.group(3);

          // If there is anything in front of the city we found, see if it
          // looks like an apt, if not it goes into place
          if (place.length() > 0) {
            String apt = parseApt(place);
            if (apt != null) {
              data.strApt = append(data.strApt, "-", apt);
            } else {
              data.strPlace = place;
            }
          }
        }
      }

      // If that didn't work, see if we can find a simple city.  This will only
      // be accepted if what is in front of it looks like a valid apt (or is
      // empty)
      if (data.strCity.length() == 0) {
        res = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, field);
        if (res.isValid()) {
          // Too bad we can't call res.getStart() before res.getData()
          // So we will have to back out the city if we decide it is not proper
          res.getData(data);
          String apt = res.getStart();
          apt = parseApt(apt);
          if (apt == null) {
            data.strCity = "";
          } else {
            data.strApt = append(data.strApt, "-", apt);
            field = res.getLeft();
          }
        }
      }

      // If we did find a city, strip off any trailing state and zip code
      if (data.strCity.length() > 0) {
        Matcher m = ST_ZIP_PTN.matcher(field);
        if (m.matches()) field = m.group(1);
      }
    }

    // No address found :(
    // See if we can pick out a city TX zip combination
    else {
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        String city = CITY_CODES.getProperty(match.group(2).toUpperCase());
        if (city != null) {
          parseAddress(match.group(1), data);
          data.strCity = city;
          field = match.group(3);
        }
      }
    }

    data.strSupp = field;
    return true;
  }
  private static final Pattern ST_ZIP_PTN = Pattern.compile("(?:(?:TX|TEXAS)\\b *)?(?:\\d{5}\\b *)(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(.*?) *\\b([A-Z]+) +(?:TX|TEXAS)(?: +\\d{5})?\\b *(.*)", Pattern.CASE_INSENSITIVE);

  private String parseApt(String apt) {
    if (apt.length() == 0) return apt;
    Matcher match = APT_PTN.matcher(apt);
    if (!match.matches()) return null;
    return match.group(1);
  }
  private static final Pattern APT_PTN = Pattern.compile(";? *(?:APT|RM|ROOM|STE|#)?(?: *#)? *([^ ]+)", Pattern.CASE_INSENSITIVE);

  @Override
  public String getProgram() {
    return super.getProgram() + " ADDR APT PLACE CITY INFO";
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ALAMO SPRINGS","COMFORT",
      "BERGHEIM",     "BOERNE",
      "LEON SPRINGS", "FAIR OAKS RANCH",
      "SISTERDALE",   "BOERNE",
      "WARING",       "BOERNE",
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "WARING",         "WARING",
      "KENDALIA",       "KENDALIA",
      "BOERNE",         "BOERNE",
      "BOERNETX",       "BOERNE",
      "BOERNETEXAS",    "BOERNE",
      "SISTERDALE",     "SISTERDALE",
      "BERGHEIM",       "BERGHEIM",
      "COMFORT",        "COMFORT",
      "COMFORTTX",      "COMFORT",
      "COMFORTTEXAS",   "COMFORT",
      "ALAMO SPRINGS",  "ALAMO SPRINGS",
      "LEON SPRINGS",   "LEON SPRINGS",
      "TX",             "TEXAS",
      "TEXAS",          "TX"
  });
}
