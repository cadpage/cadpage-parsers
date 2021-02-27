package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Hampshire County, WV
 */
public class WVHampshireCountyParser extends DispatchA48Parser {

  public WVHampshireCountyParser() {
    super(CITY_LIST, "HAMPSHIRE COUNTY", "WV", FieldType.NAME, A48_NO_CODE, UNIT_PTN);
    setupCallList(CALL_LIST);
    setupSpecialStreets(
        "HERITAGE SQUARE APTS",
        "WAPOCOMA CAMP GROUND"
    );
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@frontier.com,@hardynet.com";
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+(?:-\\d+)?|\\d+-\\d+|MEDIC", Pattern.CASE_INSENSITIVE);
  private static final Pattern COUNTY_PTN = Pattern.compile("^(HARDY|FRED|FREDERICK|MINERAL|ALLEGANY|MORGAN) ", Pattern.CASE_INSENSITIVE);
  private static final Pattern DUP_ADDR_PTN = Pattern.compile("\\d+ .* \\d{5}\\b *(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Get rid of some garbage constructs before they up the semicolon count
    // to the point where we might think this is a semicolon delimited format
    body = body.replace("Name:;", "");
    body = body.replace("Address:null null;", "");
    body = body.replace("Phone number:000000000;", "");

    if (!super.parseMsg("", body, data)) return false;

    // SO far, so good
    // Now for some special corrective measures to take if the
    // address parser did not find a city
    if (data.strCity.length() == 0) {

      // If there was no name, then see if the address includes
      // a STA word.  If found, this marks what should be the
      // end of the address and everything else goes in the name
      if (data.strName.length() == 0) {
        int pt = data.strAddress.indexOf(" STA ");
        if (pt >= 0) {
          pt += 4;
          data.strName = data.strAddress.substring(pt+1).trim();
          data.strAddress = data.strAddress.substring(0,pt).trim();
        }
      }

      // If the name looks like a neighboring county 911 center
      // leave it alone but use it to set the city & possible state
      Matcher match = COUNTY_PTN.matcher(data.strName);
      if (match.find()) {
        String city = match.group(1).toUpperCase();
        if (city.equals("FRED")) city = "FREDERICK";
        data.strCity = city + " COUNTY";
        if (city.equals("FREDERICK")) data.strState = "VA";
        else if (city.equals("ALLEGANY")) data.strState = "MD";
      }
    }

    // If we did find a city, see if it is one of our misspelled entries
    else {
      String city = MISTYPED_CITIES.getProperty(data.strCity.toUpperCase());
      if (city !=  null) data.strCity = city;
    }

    // Remove duplicated address from name field
    Matcher match = DUP_ADDR_PTN.matcher(data.strName);
    if (match.matches()) data.strName = match.group(1);

    // Look for trailing back quote in call ID.
    data.strCallId = stripFieldEnd(data.strCallId, "`");

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY ", "CITY ST ");
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "911",
      "ALLERGY",
      "ALTERED MENTAL STATUS",
      "ANIMAL BITES",
      "APPLIANCE",
      "ASSAULT",
      "ASSIST",
      "ASSISTANCE",
      "BLEED",
      "BLOOD PRESSURE",
      "BREATHING DIFFICULTY",
      "BROKE",
      "BRUSH",
      "BS",
      "CAR",
      "CARDIAC",
      "CDS",
      "CHOKE",
      "CO2 ALARM",
      "COMMERCIAL FIRE",
      "CONTROL BURN",
      "CUT",
      "DIABETIC",
      "DOMESTIC",
      "DRUNK",
      "E-ALARM",
      "ELEC",
      "F-ALARM",
      "F-COMM-ALARM",
      "FALL",
      "FIRE",
      "FLOOD",
      "FLUE",
      "GENERAL ILLNESS",
      "JUVENILE",
      "MED",
      "MVA",
      "OB",
      "OD",
      "PAIN",
      "PUBLIC SERVICE",
      "PRIORITY 4",
      "PSYCHIATRIC",
      "RESCUE",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "STAND BY",
      "STROKE",
      "STRUCTURE",
      "SUICIDE",
      "SUSPICIOUS",
      "SYNCOPE",
      "TEST",
      "TRAFFIC CONTROL",
      "TRANSFER CALLS",
      "TRAUMA",
      "TREE",
      "UNCONCIOUS",
      "UNRESPONSIVE",
      "WELL"
  );

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALLEN HILL",
    "APPLE RIDGE",
    "ARBOR MIST",
    "ARNOLD STICKLEY",
    "ASH RUCKMAN",
    "BABBLING BROOK",
    "BACK COUNTRY",
    "BACK CREEK",
    "BEAR HILL",
    "BEAR WOLLOW HOLLOW",
    "BEAVER RUN",
    "BECKS GAP",
    "BEN SAVILLE",
    "BETHEL CHURCH",
    "BILL TAYLOR",
    "BISER RIDGE",
    "BLACKS HILL",
    "BLOOMERY GREENS",
    "BRADDOCK SCHOOL",
    "BRANCH RIVER",
    "BRANDON HILL",
    "BREEZY COVE",
    "BRIGHTS HOLLOW",
    "BRISTOL SPRINGS",
    "BROWN BEAR",
    "BUCK HOLLOW",
    "BUFFALO HOLLOW",
    "BUNNY HAINES",
    "CAMP RIM ROCK",
    "CANDY BAKER",
    "CAPE COD",
    "CAPON RIVER",
    "CAPON SCHOOL",
    "CAPON SPRINGS",
    "CARDINAL RIDGE",
    "CASSIDY FARM",
    "CEDAR GROVE",
    "CHARLTON RIDGE",
    "CHRISTIAN CHURCH",
    "CHURCH HOLLOW",
    "CLARENCE TAYLOR",
    "COLD STREAM",
    "COOPER MOUNTAIN VIEW",
    "COTTAGE CASTLE",
    "COTTON TAIL",
    "CREST HAVEN",
    "CRITTON OWL HOLLOW",
    "CRYSTAL VALLEY",
    "DAVE MORELAND",
    "DEER RUN",
    "DENNIS PARKS",
    "DILLONS RUN",
    "DOCTOR RANDOLPH SPENCER",
    "DON MCCAULEY",
    "DONALDSON SCHOOL",
    "DUNMORE RIDGE",
    "DYNO NOBEL",
    "EAST VIEW",
    "ED ARNOLD",
    "EDWARD KIDWELL",
    "ENGLISH WALNUT",
    "ERVING VIEW",
    "FALCON TURN",
    "FALL SEASONS",
    "FARM VIEW",
    "FORD HILL",
    "FORK LITTLE CACAPON",
    "FOUR CORNER",
    "FOX MEADOW",
    "FRANK HAINES",
    "FRENCHES STATION",
    "FROG HOLLOW",
    "GRACES CABIN",
    "GRANNY P",
    "GRASSY LICK",
    "GREAT PLAINS",
    "GREEN LANTERN",
    "GREEN RIDGE",
    "GREENSPRING VALLEY",
    "GRIZZLY BEAR",
    "HAINES CHEROKEE TRL",
    "HAMPSHIRE PARK",
    "HARD ROCK",
    "HEIDE COOPER",
    "HEIDI COOPER",
    "HENRY W MILLER",
    "HERITAGE SKY",
    "HICKORY CORNER",
    "HICKORY HILL",
    "HILLS OF WAPOCOMA",
    "HNERY W MILLER",
    "HOOD HOLLOW",
    "HOOKS MILL",
    "IRON PILE",
    "ISLAND RIDGE",
    "JACK RUSSELL RIDGE",
    "JAKE RUCKMAN",
    "JAKE SAVILLE",
    "JERSEY MOUNTAIN",
    "JOE MILLESON",
    "JOHN PERRIS",
    "JOURNEYS END",
    "JR RANNELLS",
    "KEARNS SCHOOL HOUSE",
    "KERNS SCHOOL",
    "LAKE FERNDALE",
    "LAUREL DALE",
    "LITTLE CACAPON LEVELS",
    "LITTLE CACAPON",
    "LITTLE CACPON LEVELS",
    "MAPLE HILLS",
    "MARTINSBURG GRADE",
    "MASON HILL",
    "MCKEE HOLLOW",
    "MIDDLE RIDGE",
    "MISTY MEADOWS",
    "MONTE SINAI",
    "MOORES RUN",
    "MOSSES VALLEY",
    "MOUNT AIRY",
    "MOUNTAIN LAUREL",
    "MOUNTAIN VIEW SCHOOL",
    "MUIRWOOD GREENE",
    "MYSTIC MOUNTAIN",
    "OFFUTT SCHOOL",
    "OMBIES BARNYARD",
    "PAINTER HOLLOW",
    "PATTERSON CREEK",
    "PATTYS RUN",
    "PAW PAW",
    "PEACH TREE FARMS",
    "PIN OAK",
    "PINE LAKE",
    "POTOMAC OVERLOOK",
    "POWDER MILL",
    "POWERS HOLLOW",
    "PW LOY",
    "RAGING RIVER",
    "RAVEN RIDGE",
    "RIDGE LOOP",
    "ROBIN SPUR",
    "ROCK FACE",
    "ROCK OAK",
    "RUSSELL DALE",
    "SAINT PETER",
    "SAND FIELD",
    "SANDY HOLLOW",
    "SCENIC VIEW",
    "SCOTSMANS GLEN",
    "SHADY PINE",
    "SHADY SIDE",
    "SHORT MOUNTAIN HEIGHTS",
    "SHORT MOUNTAIN",
    "SKY MEADOWS",
    "SMOKEY HOLLOW",
    "SOL SHANHOLTZ",
    "SPRING GAP",
    "STAGGS MUSIC",
    "STONEY MOUNTAIN OVERLOOK",
    "STONEY RUN RIVER ACCESS",
    "SUNNY DALE ACRES",
    "TEABERRY RIDGE",
    "THREE CHURCHES",
    "TIMBER MOUNTAIN",
    "TIMBER RIDGE CAMP",
    "TIMBER RIDGE",
    "TROUT RUN",
    "TUCKER RIDGE",
    "TURKEY HILL",
    "TURKEY RIDGE",
    "VALLEY VIEW",
    "WAPACOMA CAMPGROUND",
    "WAPOCOMA CAMPGROUND",
    "WARDEB HOLLOW WEST",
    "WARDEN LAKE DE",
    "WARNES CIRCLE",
    "WATER FRONT",
    "WATSON SCHOOL",
    "WHISPERING RIDGE",
    "WHITE PINE RIDGE",
    "WILD BOAR",
    "WILD TURKEY",
    "WINCHESTER GRADE",
    "WOODLAND MOUNTAIN",
    "ZEKI HALICI"
  };

  private static final String[] CITY_LIST = new String[]{

    // Incorporated Communities
    "ROMNEY",
    "CAPON BRIDGE",

    // Unincorporated Communities
    "AUGUSTA",
    "BARNES MILL",
    "BLOOMERY",
    "BLUES BEACH",
    "BUBBLING SPRING",
    "CAPON LAKE",
    "CAPON SPRING",
    "CAPON SPRINGS",
    "CAPON SPRINGS STATION",
    "COLD STREAM",
    "CREEKVALE",
    "DAVIS FORD",
    "DELRAY",
    "DILLONS RUN",
    "DONALDSON",
    "FORKS OF CACAPON",
    "FRENCHBURG",
    "GLEBE",
    "GOOD",
    "GRACE",
    "GREEN SPRING",
        "GREENSPRING",   // Mistyped
    "HAINESVILLE",
    "HANGING ROCK",
    "HIGGINSVILLE",
    "HIGH VIEW",
    "HOOKS MILLS",
    "HOY",
    "INTERMONT",
    "JERICHO",
    "JUNCTION",
    "KIRBY",
    "LARGENT",
    "LEHEW",
    "LEVELS",
    "LITTLE CACAPON",
    "LOOM",
    "MECHANICSBURG",
    "MILLBROOK",
    "MILLEN",
    "MILLESONS MILL",
    "NEALS RUN",
    "NERO",
    "NORTH RIVER MILLS",
    "OKONOKO",
    "PANCAKE",
    "PIN OAK",
    "PLEASANT DALE",
    "POINTS",
    "PURGITSVILLE",
        "PURGITVILLE",    // Mistyped
    "RADA",
    "RAVEN ROCKS",
    "RIDGEDALE",
    "RIO",
    "RUCKMAN",
    "SECTOR",
    "SEDAN",
    "SHANKS",
    "SHILOH",
    "SLANESVILLE",
    "SOUTH BRANCH DEPOT",
    "SPRINGFIELD",
        "SPARINGFIELD",  // Mistyped
    "THREE CHURCHES",
    "VANCE",
    "VANDERLIP",
    "WAPPOCOMO",
    "WOODROW",
    "YELLOW SPRING",
        "YELLOW SPRIN",
        "YELLOW SPRINGS",

    // Hardy County
    "WARDENSVILLE",
        "WARDENSVILE",

    // Morgan County
    "PAW PAW",

    // Mineral County
    "BURLINGTON",
  };

  private static final Properties MISTYPED_CITIES = buildCodeTable(new String[]{
      "GREENSPRING",        "GREEN SPRING",
      "PURGITVILLE",        "PURGITSVILLE",
      "SPARINGFIELD",       "SPRINGFIELD",
      "WARDENSVILE",        "WARDENSVILLE",
      "YELLOW SPRIN",       "YELLOW SPRING",
      "YELLOW SPRINGS",     "YELLOW SPRING"

  });
}
