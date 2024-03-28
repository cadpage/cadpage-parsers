package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDWorcesterCountyParser extends GroupBestParser {

  public MDWorcesterCountyParser() {
    super(new MDWorcesterCountyAParser(), new MDWorcesterCountyBParser());
  }

  public static void fixCity(Data data) {
    data.strCity = convertCodes(data.strCity, FIX_CITY_TABLE);
    String city = data.strCity.toUpperCase();
    if (VA_CITY_SET.contains(city)) {
      data.strState = "VA";
    }
    else if (DE_CITY_SET.contains(city)) {
      data.strState = "DE";
    }
  }

  public static final String[] CITY_LIST = new String[]{
    "POCO",       // Typo
    "POCOMOK",    // Typo
    "POCOMOKE",
    "BERLIN",
    "OCEAN CITY",
    "SNOW HILL",
    "BISHOPVILLE",
    "GIRDLETREE",
    "NEWARK",
    "OCEAN PINES",
    "SHOWELL",
    "STOCKTON",
    "WEST OCEAN CITY",
    "WHALEYVILLE",
    "BOXION",
    "CEDARTOWN",
    "FRIENDSHIP",
    "GERMANTOWN",
    "GOODWILL",
    "KLEJ GRANGE",
    "LIBERTOWN",
    "NASSAWANGO HILLS",
    "POPLARTOWN",
    "PUBLIC LANDING",
    "SALISBURY",
    "SINNEPUXENT",
    "SOUTH POINT",
    "TAYLORVILLE",
    "WHITEON",

    "BERL",
    "SH",
    "WHAL",


    // Somerset County
    "CRISFIELD",
    "WESTOVER",
    "PRINCESS ANNE",

    // Wicomico County
    "SALISBURY",

    // Accomack County, VA
    "ATLANTIC",
    "CHINCOTEAGUE",
    "GREENBACKVILLE",
    "HORTOWN",     // typo
    "HORNTOWN",
    "MARIONVILLE",
    "NEW CHURCH",
    "OAK HALL",
    "PARKSL",
    "PARKSLEY",
    "TEMPERANCEVILLE",

    // Northampton County, VA
    "EXMORE",
    "MARIONVILLE",

    // Sussex County, DE
    "SELBYVILLE"

  };

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "HORTOWN",    "HORNTOWN",
      "PARKSL",     "PARKSLEY",
      "POCO",       "POCOMOKE",
      "POCOMOK",    "POCOMOKE",

      "BERL",       "BERLIN",
      "SH",         "SNOW HILL",
      "WHAL",       "WHALEYVILLE"
  });

  private static final Set<String> VA_CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "ATLANTIC",
      "CHINCOTEAGUE",
      "GREENBACKVILLE",
      "HORNTOWN",
      "NEW CHURCH",
      "OAK HALL",
      "PARKSLEY",
      "TEMPERANCEVILLE",

      "EXMORE",
      "MARIONVILLE"
  }));

  private static final Set<String> DE_CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "SELBYVILLE"
  }));
}
