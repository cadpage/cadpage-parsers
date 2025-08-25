package net.anei.cadpage.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class takes care of allocating the correct parser for a location code
 */
public class ManageParsers {

  // The name of the general alert parser code
  private static final String ALERT_PARSER = "GeneralAlert";

  // Table mapping known location codes to their corresponding parser
  private Map<String, MsgParser> parserMap = new HashMap<String, MsgParser>();

  private String curLocCode = null;
  private MsgParser curParser = null;

  private boolean testMode = false;

  // Private constructor, no body can build this except getInstance()
  private ManageParsers() {}

  public void setTestMode(boolean testMode) {
    this.testMode = testMode;
  }

  /**
   * Get parser corresponding to location code
   * @param location requested location code or null to use current config setting
   * @return requested parser
   */
  public MsgParser getParser(String location) {
    return getParser(location, null, null);
  }

  /**
   * Get parser corresponding to location code
   * @param location requested location code or null to use current config setting
   * @param body message body if known, otherwise null
   * @return requested parser
   */
  public MsgParser getParser(String location, String subject, String body) {

    // No real parser codes start with "X".  If we are given one, then it
    // must come from Active911 which sometimes adds an "X" prefix for their
    // own purposes
    if (location != null && location.startsWith("X")) location = location.substring(1);

    // Convert any old codes that have been renamed to new values
    // This is suppressed in test mode so we can catch hard coded referenced to
    // decomisioned parsers
    if (!testMode) location = convertLocationCode(location, subject, body);

    // First level cache.  If location code matches what we have stored for
    // the current location code, return the current parser
    if (location.equals(curLocCode)) return curParser;

    // Second level cache, see if it is the our table of parsers by location
    MsgParser parser = parserMap.get(location);
    if (parser == null) {

      // Otherwise we need to create a new parser
      // First see if there are multiple location parsers
      if (location.contains(",")) {

        // If there are, call ourselves recursively to allocate each
        // individual parser
        String[] locationList = location.split(",");
        MsgParser[] parserList = new MsgParser[locationList.length];
        for (int ii = 0; ii<locationList.length; ii++) {
          parserList[ii] = getParser(locationList[ii]);
        }
        parser = new GroupBestParser(parserList);
      }

      // Otherwise find the parser class and instantiate it
      else {
        String className = getParserClassname(location);
        try {
          parser = (MsgParser)Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
          throw new RuntimeException("Failed to instantiate " + className + '\n' + ex.getMessage(), ex);
        }
      }

      // Then save the location and parser in our parser table
      parserMap.put(location, parser);
    }

    // Before we return the parser we found, save parser in primary cache
    curLocCode = location;
    curParser = parser;
    return parser;
  }

  /**
   * Get fully qualified parser class name associated with location
   * @param location requested location
   * @return parser class name
   */
  private String getParserClassname(String location) {

    String pkg = null;
    if (location.length() >= 3) {
      if (location.startsWith("ZCA")) {
        pkg = location.substring(0,5);
      } else if (location.startsWith("Z")) {
        pkg = location.substring(0,3);
      } else if (Character.isUpperCase(location.charAt(1))) {
        pkg = location.substring(0,2);
      } else if (location.startsWith("Dispatch")) {
        pkg = "dispatch";
      } else if (location.startsWith("General") || location.startsWith("Standard") || location.equals("AiviaAED")) {
        pkg = "general";
      }
    }
    StringBuffer sb = new StringBuffer();
    sb.append(this.getClass().getPackage().getName());
    sb.append('.');
    if (pkg != null) {
      sb.append(pkg);
      sb.append('.');
    }
    sb.append(location);
    sb.append("Parser");
    return sb.toString();

  }

  /**
   * @return parser used to process general alerts (no parsing no address)
   */
  public MsgParser getAlertParser() {
    return getParser(ALERT_PARSER);
  }

  /**
   * Return location name associated with location code
   * @param location location code
   * @return location name
   */
  public String getLocName(String location) {

    // Location shouldn't be null, but upstream bugs are setting it this
    // so at least don't die over it
    if (location == null) return "";

    return getParser(location).getLocName();
  }

  private static ManageParsers instance = new ManageParsers();

  /**
   * @return singleton instance of ManageParsers
   */
  public static ManageParsers getInstance() {
    return instance;
  }

  /**
   * Convert old codes that have been renamed to something else
   * @param location requested location code
   * @return possibly updated location code
   */
  public static String convertLocationCode(String location) {
    return convertLocationCode(location, null, null);
  }

  /**
   * Convert old codes that have been renamed to something else
   * @param location requested location code
   * @param body message body if known, null otherwise
   * @return possibly updated location code
   */
  public static String convertLocationCode(String location, String subject, String body) {
      String result = OLD_CODE_TABLE.getProperty(location);
      if (result != null) return result;

    // And another from NYOneidaCounty to NYMadisonCountyB from 01/16/2018
    if (subject != null && location.equals("NYOneidaCounty") && subject.equals("SEVAC")) return "NYMadisonCountyB";

    // And from NCMaconCountyB to NCMaconCountyD on 11/17/2019
    if (location.equals("NCMaconCountyB") && body.startsWith("MACON")) location = "NCMaconCountyD";

    return location;
  }

  public boolean isDecomissioned(String location) {
    return OLD_CODE_TABLE.containsKey(location);
  }

  // fixed map mapping old to new location codes
  private static final Properties OLD_CODE_TABLE = MsgParser.buildCodeTable(new String[]{
        "COClearCreekCountyB","ClearCreekCountyA",       // 05/02/2023
        "CORioBlancoCountyB", "CORioBlancoCountyB",
        "GAMurrayCountyA",    "GAMurrayCountyB",
        "CACalaverasCountyA", "CACalaverasCountyB",      // 05/30/2023
        "CATuolumneCountyA",  "CATuolumneCountyB",
        "PAArmstrongCountyA", "General",                 // 06/03/2023
        "PACumberlandCountyA","General",
        "PABerksCountyA",     "General",
        "PAPhiladelphia",     "General",                 // 06/12/2023
        "MDPrinceGeorgesCountyE", "General",
        "MOAndrewCountyA",    "MOAndrewCountyB",         // 06/26/2023
        "MOPerryCountyA",     "MOPerryCountyB",
        "KYMarshallCountyA",  "General",                 // 07/03/2023
        "KYMarshallCountyB",  "General",
        "ALDothanA",          "General",
        "KYStatePoliceA",     "General",
        "OHWarrenCountyA",    "General",
        "MOStLouisCountyG",   "General",
        "KYKnoxCountyA",      "General",                 // 08/22/2023
        "MOMonett",           "MOLawrenceCounty",        // 09/05/2023
        "PAPennStar",         "General",                 // 09/28/2023
        "PAAdamsCountyB",     "General",                 // 10/12/2023
        "PAColbertCountyB",   "General",                 // 10/16/2023
        "KYKnoxCountyC",      "KYKnoxCountyB",           // 12/07/2023
        "MNDouglasCountyA",   "MNDouglasCountyB",
        "TXHaysCountyC",      "TXHaysCountyB",
        "ARPulaskiCountyA",   "General",                 // 12/17/2023
        "ARPulaskiCountyB",   "General",
        "OHClarkCountyA",     "General",                 // 01/02/2024
        "OHClarkCountyB",     "General",
        "OHClarkCountyD",     "General",
        "OHSummitCountyF",    "General",                 // 01/08/2024
        "PALackawannaCountyA","General",                 // 01/29/2024
        "NCHarnettCountyB",   "General",                 // 02/16/2024
        "ILMassacCountyB",    "General",                 // 02/17/2024
        "OKMuskogeeCounty",   "General",                 // 02/26/2024
        "PAArmstrongCountyB", "General",                 // 03/17/2024
        "PAFranklinCountyB",  "General",                 // 04/01/2024
        "OHCuyahogaCountyC",  "General",                 // 04/09/2024
        "ILLakeCountyB",      "General",                 // 05/13/2024
        "CTWaterfordTown",    "CTNewLondonCounty",       // 06/03/2024
        "AZYavapaiCountyD",   "AZYavapaiCountyE",        // 06/04/2024
        "ORYamhillCountyB",   "General",                 // 06/28/2024
        "MIGrandTraverseCounty", "General",              // 07/16/2024
        "NCGastonCountyB",    "General",                 // 07/27/2024
        "ARPulaskiCountyF",   "ARPulaskiCountyE",        // 09/10/2024
        "ORDouglasCountyD",   "General",                 // 09/16/2024
        "ARGrantCounty",      "General",                 // 09/16/2024
        "ALStClairCountyA",   "General",                 // 10/02/2024
        "WAGraysHarborCountyA","WAGraysHarborCountyB",   // 10/07/2024
        "PABerksCountyB",     "PABerksCountyD",          // 10/07/2024
        "WAWhitmanCountyA",   "General",                 // 11/08/2024
        "NCStanlyCountyB",    "General",                 // 11/12/2024
        "TXCookeCountyA",     "General",                 // 11/18/2024
        "TXHaysCountyA",      "General",                 // 01/28/2025
        "PASomersetCountyA",  "General",                 // 02/03/2025
        "KYBourbonCountyB",   "KYBourbonCountyA",        // 02/11/2025
        "NCIredellCountyA",   "General",                 // 02/19/2025
        "OHMuskingumCountyA", "General",                 // 02/24/2025
        "OHMuskingumCountyB", "General",
        "OHMuskingumCountyE", "General",
        "MSHarrisonCountyB",  "General",                 // 03/10/2025
        "TXLeagueCity",       "TXHarrisCounty",          // 03/20/2025
        "TXWebster",          "TXHarrisCounty",
        "MOCapeGirardeauCountyE","MOCapeGirardeauCountyC", // 03/25/2025
        "OHMorrowCountyA",    "General",                 // 03/31/2025
        "NCCumberlandCountyA","General",                 // 05/27/2025
        "PAChesterCountyD1",  "PAChesterCountyO",        // 06/02/2025
        "PAAlleghenyCountyD", "General",                 //06/09/2025
        "VACampbellCountyA",  "General",                 // 06/30/2025
        "MDAnneArundelCountyAnnapolis", "General",       // 06/30/2025
        "LATangipahoaParishA","General",                 // 07/17/2025
        "KYDaviessCountyA",   "General",                 // 07/28/2025
        "KYCaldwellCountyA",  "General"                 // 08/25/2025
  });

}
