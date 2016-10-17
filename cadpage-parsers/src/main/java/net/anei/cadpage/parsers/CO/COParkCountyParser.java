package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

public class COParkCountyParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("CAD[ /]+");
  private static final Pattern PLACE_PTN = Pattern.compile("(.*)\\((.*)\\)(.*)");
  
  public COParkCountyParser() {
    super(CITY_LIST, "PARK COUNTY", "CO");
    setFieldList("ADDR APT PLACE CITY CALL");
  }
  
  @Override
  public String getFilter() {
    return "parkcodispatch@parkco.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // There should be a marker, but very occasionally there is not
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    else if (!isPositiveId()) return false;
    
    // We have a couple tricks.  First look for a place name in parens.  If found
    // it also marks the beginning of the call description
    match = PLACE_PTN.matcher(body);
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
    
    // If we found a city name, check to see if it is really a subdivision name
    // If it is move it to place and save the real city.
    if (data.strCity.length() > 0) {
      String city = CITY_PLACE_TABLE.getProperty(data.strCity);
      if (city != null) {
        data.strPlace = append(data.strCity, " - ", data.strPlace);
        data.strCity = city;
      }
    }
    return true;
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
  
  private CodeSet CALL_LIST = new ReverseCodeSet(
      "ACCIDENT/ CRASH",
      "ACCIDENT/CRASH",
      "ACCIDETN/CRASH",
      "ALARM CARBON MONOXIDE",
      "ALARM FIRE",
      "ALARM SMOKE",
      "FIRE ILLEGAL CAMPFIRE",
      "FIRE MISC",
      "FIRE STRUCTURE",
      "GAS ODOR/LEAK",
      "MEDICAL",
      "SMOKE REPORT",
      "VEHICLE SLIDE OFF"
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
  
  private static final Properties CITY_PLACE_TABLE = buildCodeTable(new String[]{
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
