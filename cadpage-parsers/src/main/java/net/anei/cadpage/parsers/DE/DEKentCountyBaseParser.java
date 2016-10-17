package net.anei.cadpage.parsers.DE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DEKentCountyBaseParser extends FieldProgramParser {
  
  public DEKentCountyBaseParser(String defCity, String defState, String program) {
    super(CITY_LIST, defCity, defState, program);
    setupMultiWordStreets(PROTECT_STREET_LIST);
    setupProtectedNames(MULTI_WORD_STREET_LIST);
  }
  
  /**
   * Set state field if required
   * @param data parsed data object
   */
   static void adjustCityState(Data data) {
    
    // Expand abbreviated city codes
    String upCity = data.strCity.toUpperCase();
    
    String city = CITY_CODES.getProperty(upCity);
    if (city != null) {
      data.strCity = city;
      upCity = city.toUpperCase();
    }
    
    // Some city names are subdivisons or apartment complexes
    city = PLACE_NAMES.getProperty(upCity);
    if (city != null) {
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = city;
    }
    
    String state = CITY_STATE_TABLE.getProperty(data.strCity.toUpperCase());
    if (state != null) data.strState = state;
  }
   
  static final String[] PROTECT_STREET_LIST = new String[]{
    "GUN AND ROD",
    "SLOW AND EASY",

  }; 
   
  static final String[] MULTI_WORD_STREET_LIST = new String[]{
    "COUNTRY ROAD", 
    "GUN AND ROD",
    "PARADISE ALLEY",
    "SLOW AND EASY"
  }; 
  
  static final String[] CITY_LIST = new String[]{
    
    // Cities
    "DOVER",
    "HARRINGTON",
    "MILFORD",
    
    // Towns
    "BOWERS",
    "CAMDEN",
    "CAMDEN WYOMING",
    "CHESWOLD",
    "CLAYTON",
    "FARMINGTON",
    "FELTON",
    "FREDERICA",
    "HARTLY",
    "HOUSTON",
    "KENTON",
    "LEIPSIC",
    "LITTLE CREEK",
    "MAGNOLIA",
    "SMYRNA",
    "VIOLA",
    "WOODSIDE",
    "WYOMING",
    
    // Census - designated places
    "DOVER AIR FORCE BASE",
    "HIGHLAND ACRES",
    "KENT ACRES",
    "RISING SUN-LEBANON",
    "RIVERVIEW",
    "RODNEY VILLAGE",
    "WOODSIDE EAST",

    // Other localities
    "ANDREWVILLE",
    "BERRYTOWN",
    "LITTLE HEAVEN",
    "MARYDEL",
    
    // Carolin County, MD
    "DENTON",
    "FEDERALSBURG",
    "GREENSBORO",
    "HENDERSON",
    "HILLSBORO",
    "MARYDEL",
    "PRESTON",
    "RIDGELY",
    
    // Kent County, MD
    "BETTERTON",
    "CHESTERTOWN",
    "GALENA",
    "MILLINGTON",
    "PENNSVILLE",
    "ROCK HALL",
    
    // Somerset County, MD
    "PRINCESS ANNE",
    
    // Sussex County
    "BETHANY BEACH", 
    "BETHEL", 
    "BLADES", 
    "BRIDGEVILLE", 
    "DAGSBORO", 
    "DELMAR",
    "DEWEY BEACH", 
    "ELLENDALE", 
    "FENWICK ISLAND", 
    "FRANKFORD", 
    "GEORGETOWN",
    "GREENWOOD",
    "GUMBORO",
    "HARBESON",
    "HENLOPEN ACRES", 
    "LAUREL", 
    "LEWES", 
    "LINCOLN",
    "LONG NECK",
    "MILFORD",
    "MILB",           // abbrv for MILLSBORO
    "MILLSBORO",
    "MILLVILLE", 
    "MILTON", 
    "OAK ORCHARD",
    "OCEAN VIEW", 
    "REHOBOTH",
    "REHOBOTH BEACH",
    "ROXANA",
    "SEAFORD", 
    "SELBYVILLE",
    "SLAUGHTER BEACH", 
    "SOUTH BETHANY",
    
    // Wicomico County, MD
    "FRUITLAND",
    "PARSONSBURG",
    "PITTSVILLE",
    "SALISBURY",
    
    // Worcester County, MD
    "BISHOPVILLE",
    
    // Neighborhood names in the PLACE_NAMES table
    "ANGOLA BY THE BAY",
    "ANGOLA CREST",
    "ANGOLA ESTATES",
    "ANGOLA NECK PARK",
    "ANN ACRES",
    "ARNELL CREEK",
    "ASPEN MEADOWS",
    "BAY HARBOR",
    "BAY POINTE",
    "BEACHFIELD",
    "BALD EAGLE VILLAGE",
    "BAY FRONT",
    "BAY OAKS",
    "BAY RIDGE WOODS",
    "BAY VISTA",
    "BELLA VISTA",
    "BLUE POINT VILLAS",
    "BOOKHAMMER",
    "BOOKHAMMER ESTATES",
    "BEACH HAVEN",
    "BREAKWATER ESTATES",
    "BREEZEWOOD",
    "BURTON VILLAGE",
    "CAMELOT",
    "CANAL CORKRAN",
    "CANAL POINT",
    "CAPTIVA SANDS",
    "CEDAR VALLEY",
    "CHERRY CREEK VALLEY",
    "CHERRY WALK",
    "COUNTRY MANOR",
    "CREEKWOOD",
    "DOLLYS",
    "EAGLES LANDING",
    "EDGEWATER PARK",
    "ESTATES OF SEA CHASE",
    "FIELDWOOD",
    "HAPPY GO LUCKY",
    "HARMON BAY",
    "HENLOPEN ACRES",
    "HENLOPEN KEYS",
    "HERRING LANDING",
    "HOLLAND GLADE",
    "HOLLY OAK ACRES",
    "HOLLY WOOD",
    "INDIAN BEACH",
    "JOY BEACH",
    "KEYS OF MARSH HARBOR",
    "KINGS CREEK",
    "KINSALE GLEN",
    "KYRIE ESTATES",
    "LAZY PINE RETREAT",
    "LOVE CREEK",
    "LOVE CREEK WOODS",
    "MASTEN HEIGHTS",
    "MULBERRY KNOLL",
    "OLD LANDING",
    "OLD LANDING WOODS",
    "PINE VALLEY",
    "PINEY GLADE",
    "PORT DELMARVA",
    "RBYCC",
    "REHOBOTH BAY",
    "REHOBOTH BEACH GARDENS",
    "REHOBOTH CROSSING",
    "REHOBOTH MANOR",
    "REHOBOTH SHORE ESTATES",
    "SANDALWOOD",
    "SANIBEL VILLAGE",
    "SAWGRASS",
    "SEA AIR VILLAGE",
    "SEA CHASE",
    "SEA COAST COURTS",
    "SEABRIGHT",
    "SHADY RIDGE",
    "SHADY GROVE",
    "SILVER LAKE MANOR",
    "SILVER LAKE SHORES",
    "SILVER VIEW",
    "SPRING LAKE",
    "ST MICHAELS PLACE",
    "STABLE FARM",
    "STONEWOOD CHASE",
    "TALL PINES",
    "THE AVENUE",
    "THE CHANCELLERY",
    "THE COVE",
    "THE LANDING",
    "THE SEASONS",
    "THE PALMS OF REHOBOTH",
    "THE TIDES",
    "THE VILLAGES AT HERRING CREEK",
    "THE VILLAGES OF OLD LANDING",
    "THE WOODS AT SEASIDE",
    "TRU VALE ACRES",
    "VILLAGES AT HERRING CREEK",
    "WASHINGTON HEIGHTS",
    "WEST BAY PARK"

  };

  protected static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(CITY_LIST));
  
  // Abbreviated city codes
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MILB",        "Millsboro"
  });
  
  // Out of state municipalities
  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      
      // Carolin County
      "DENTON",       "MD",
      "FEDERALSBURG", "MD",
      "GREENSBORO",   "MD",
      "HENDERSON",    "MD",
      "HILLSBORO",    "MD",
      "MARYDEL",      "MD",
      "PRESTON",      "MD",
      "RIDGELY",      "MD",
      
      // Kent County
      "BETTERTON",    "MD",
      "CHESTERTOWN",  "MD",
      "GALENA",       "MD",
      "MILLINGTON",   "MD",
      "ROCK HALL",    "MD",
      
      // Somerset County
      "PRINCESS ANNE","MD",
      
      // Wicomico County
      "FRUITLAND",    "MD",
      "PARSONSBURG",  "MD",
      "PITTSVILLE",   "MD",
      "SALISBURY",    "MD",
      
      // Worcester County
      "BISHOPVILLE",  "MD"

  });
  
  // City names that are really place names mapped to actual city name
  private static final Properties PLACE_NAMES = buildCodeTable(new String[]{
      "ANGOLA BY THE BAY",            "REHOBOTH BEACH",
      "ANGOLA CREST",                 "LEWE",
      "ANGOLA ESTATES",               "LEWE",
      "ANGOLA NECK PARK",             "LEWE",
      "ANN ACRES",                    "REHOBOTH BEACH",
      "ARNELL CREEK",                 "REHOBOTH BEACH",
      "ASPEN MEADOWS",                "REHOBOTH BEACH",
      "BAY HARBOR",                   "REHOBOTH BEACH",
      "BAY POINTE",                   "LEWE",
      "BEACHFIELD",                   "REHOBOTH BEACH",
      "BALD EAGLE VILLAGE",           "REHOBOTH BEACH",
      "BAY FRONT",                    "LEWE",
      "BAY OAKS",                     "LEWE",
      "BAY RIDGE WOODS",              "LEWE",
      "BAY VISTA",                    "REHOBOTH BEACH",
      "BELLA VISTA",                  "REHOBOTH BEACH",
      "BLUE POINT VILLAS",            "REHOBOTH BEACH",
      "BOOKHAMMER",                   "LEWE",
      "BOOKHAMMER ESTATES",           "LEWE",
      "BEACH HAVEN",                  "REHOBOTH BEACH",
      "BREAKWATER ESTATES",           "REHOBOTH BEACH",
      "BREEZEWOOD",                   "REHOBOTH BEACH",
      "BURTON VILLAGE",               "REHOBOTH BEACH",
      "CAMELOT",                      "REHOBOTH BEACH",
      "CANAL CORKRAN",                "REHOBOTH BEACH",
      "CANAL POINT",                  "REHOBOTH BEACH",
      "CAPTIVA SANDS",                "REHOBOTH BEACH",
      "CEDAR VALLEY",                 "REHOBOTH BEACH",
      "CHERRY CREEK VALLEY",          "LEWE",
      "CHERRY WALK",                  "LEWE",
      "COUNTRY MANOR",                "REHOBOTH BEACH",
      "CREEKWOOD",                    "REHOBOTH BEACH",
      "DOLLYS",                       "LEWE",
      "EAGLES LANDING",               "REHOBOTH BEACH",
      "EDGEWATER PARK",               "REHOBOTH BEACH",
      "ESTATES OF SEA CHASE",         "REHOBOTH BEACH",
      "FIELDWOOD",                    "REHOBOTH BEACH",
      "HAPPY GO LUCKY",               "LEWE",
      "HARMON BAY",                   "REHOBOTH BEACH",
      "HENLOPEN ACRES",               "REHOBOTH BEACH",
      "HENLOPEN KEYS",                "REHOBOTH BEACH",
      "HERRING LANDING",              "LEWE",
      "HOLLAND GLADE",                "REHOBOTH BEACH",
      "HOLLY OAK ACRES",              "LEWE",
      "HOLLY WOOD",                   "LEWE",
      "INDIAN BEACH",                 "REHOBOTH BEACH",
      "JOY BEACH",                    "LEWE",
      "KEYS OF MARSH HARBOR",         "REHOBOTH BEACH",
      "KINGS CREEK",                  "REHOBOTH BEACH",
      "KINSALE GLEN",                 "REHOBOTH BEACH",
      "KYRIE ESTATES",                "REHOBOTH BEACH",
      "LAZY PINE RETREAT",            "LEWE",
      "LOVE CREEK",                   "LEWE",
      "LOVE CREEK WOODS",             "LEWE",
      "MASTEN HEIGHTS",               "REHOBOTH BEACH",
      "MULBERRY KNOLL",               "LEWE",
      "OLD LANDING",                  "REHOBOTH BEACH",
      "OLD LANDING WOODS",            "REHOBOTH BEACH",
      "PINE VALLEY",                  "REHOBOTH BEACH",
      "PINEY GLADE",                  "REHOBOTH BEACH",
      "PORT DELMARVA",                "REHOBOTH BEACH",
      "RBYCC",                        "REHOBOTH BEACH",
      "REHOBOTH BAY",                 "REHOBOTH BEACH",
      "REHOBOTH BEACH GARDENS",       "REHOBOTH BEACH",
      "REHOBOTH CROSSING",            "REHOBOTH BEACH",
      "REHOBOTH MANOR",               "REHOBOTH BEACH",
      "REHOBOTH SHORE ESTATES",       "REHOBOTH BEACH",
      "SANDALWOOD",                   "REHOBOTH BEACH",
      "SANIBEL VILLAGE",              "REHOBOTH BEACH",
      "SAWGRASS",                     "REHOBOTH BEACH",
      "SEA AIR VILLAGE",              "REHOBOTH BEACH",
      "SEA CHASE",                    "REHOBOTH BEACH",
      "SEA COAST COURTS",             "REHOBOTH BEACH",
      "SEABRIGHT",                    "REHOBOTH BEACH",
      "SHADY RIDGE",                  "REHOBOTH BEACH",
      "SHADY GROVE",                  "REHOBOTH BEACH",
      "SILVER LAKE MANOR",            "REHOBOTH BEACH",
      "SILVER LAKE SHORES",           "REHOBOTH BEACH",
      "SILVER VIEW",                  "REHOBOTH BEACH",
      "SPRING LAKE",                  "REHOBOTH BEACH",
      "ST MICHAELS PLACE",            "REHOBOTH BEACH",
      "STABLE FARM",                  "REHOBOTH BEACH",
      "STONEWOOD CHASE",              "REHOBOTH BEACH",
      "TALL PINES",                   "LEWE",
      "THE AVENUE",                   "REHOBOTH BEACH",
      "THE CHANCELLERY",              "REHOBOTH BEACH",
      "THE COVE",                     "LEWE",
      "THE LANDING",                  "REHOBOTH BEACH",
      "THE SEASONS",                  "REHOBOTH BEACH",
      "THE PALMS OF REHOBOTH",        "REHOBOTH BEACH",
      "THE TIDES",                    "REHOBOTH BEACH",
      "THE VILLAGES AT HERRING CREEK","LEWE",
      "THE VILLAGES OF OLD LANDING",  "REHOBOTH BEACH",
      "THE WOODS AT SEASIDE",         "REHOBOTH BEACH",
      "TRU VALE ACRES",               "REHOBOTH BEACH",
      "VILLAGES AT HERRING CREEK",    "LEWE",
      "WASHINGTON HEIGHTS",           "REHOBOTH BEACH",
      "WEST BAY PARK",                "LEWE"
  });
}


