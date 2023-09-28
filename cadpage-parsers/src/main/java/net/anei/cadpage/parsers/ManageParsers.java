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

  // Private constructor, no body can build this except getInstance()
  private ManageParsers() {}

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
    location = convertLocationCode(location, subject, body);

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

  // fixed map mapping old to new location codes
  private static final Properties OLD_CODE_TABLE = MsgParser.buildCodeTable(new String[]{
        "CAStockton",         "CASanJoaquinCounty",      // 05/21/2021
        "SDKiowaCounty",      "KSKiowaCounty",           // 06/07/2021
        "TXSeguin",           "TXGuadalupeCounty",       // 11/09/2021
        "CTOldSaybroook",     "CTMiddlesexCounty",       // 11/22/2021
        "SDMinnehahaCountyC", "CTMiddlesexCounty",       // 11/22/2021 Active911 oops
        "OHHudson",           "OHSummitCounty",          // 01/16/2022
        "MSHernando",         "MSDesotoCounty",          // 02/21/2022
        "NCLumberton",        "NCRobesonCounty",         // 10/18/2022
        "TXDallasCountyG",    "TXDallasCountyC",         // 11/01/2022
        "OHMontgomeryCountyC2","OHMontgomeryCountyA",    // 12/19/2022
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
        "PAPennStar",         "General"                 // 09/28/2023
  });

}
