package net.anei.cadpage.parsers.MO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOBarryCountyParser extends GroupBestParser {

  public MOBarryCountyParser() {
    super(new MOBarryCountyAParser(), new MOBarryCountyCParser());
  }
  
  private static final Pattern ALT_APT_PTN = Pattern.compile("\\b(LOT|UNIT|SUITE)\\b");
  private static final Pattern RD_PTN = Pattern.compile("\\bRD\\b");

  public static String fixGpsLookupAddress(String address, String apt) {
    address = address.toUpperCase();
    if (GPS_LOOKUP_APT.contains(address)) {
      if (apt.length() > 0) {
        address = append(address, " APT ", apt.toUpperCase());
      } else {
        address = ALT_APT_PTN.matcher(address).replaceAll("APT");
      }
    }
    address = RD_PTN.matcher(address).replaceAll("ROAD");
    return address;
  }
  
  private static final Set<String> GPS_LOOKUP_APT = new  HashSet<String>(Arrays.asList(
      "500 BUSINESS 37",
      "1487 FARM ROAD 1050",
      "1507 FARM ROAD 1050",
      "1192 FARM ROAD 1060",
      "28267 FARM ROAD 1075",
      "27840 FARM ROAD 1162",
      "10281 FARM ROAD 2000",
      "101 GLENDA ST",
      "103 GLENDA ST",
      "105 GLENDA ST",
      "107 GLENDA ST",
      "109 GLENDA ST",
      "112 GLENDA ST",
      "113 GLENDA ST",
      "115 GLENDA ST",
      "121 GLENDA ST",
      "123 GLENDA ST",
      "401 MAIN ST",
      "1009 MAIN ST",
      "27910 MAIN ST",
      "29016 MAIN ST",
      "10607 N CHESTNUT ST",
      "25905 OZARK VILLAS RD",
      "19824 STATE HIGHWAY 112",
      "12207 STATE HIGHWAY 248",
      "7483 STATE HIGHWAY 37",
      "20528 STATE HIGHWAY 37",
      "30717 STATE HIGHWAY 37",
      "30889 STATE HIGHWAY 37",
      "30898 STATE HIGHWAY 37",
      "16030 STATE HIGHWAY 37 BUS",
      "20784 STATE HIGHWAY 86",
      "30892 STATE HIGHWAY 86",
      "12760 STATE HIGHWAY AA",
      "8061 STATE HIGHWAY BB",
      "8567 STATE HIGHWAY C",
      "29724 STATE HIGHWAY J",
      "16796 STATE HIGHWAY Y",
      "27 SYCAMORE DR",
      "29 SYCAMORE DR",
      "7490 W ROLLER RDG",
      "7535 W ROLLER RDG",
      "405 W STATE HIGHWAY C"
  ));

  static final Properties GPS_LOOKUP_TABLE;
  static {
    GPS_LOOKUP_TABLE = new Properties();
    MOBarryCountyGPSTable1.buildGPSTable(GPS_LOOKUP_TABLE);
    MOBarryCountyGPSTable2.buildGPSTable(GPS_LOOKUP_TABLE);
  }

  
  static final String[] CITY_LIST = new String[]{
    "COUNTY",
    "BARRY COUNTY",

    "CASSVILLE",
    "EXETER",
    "MONETT",
    "PIERCE CITY",
    "PURDY",
    "SELIGMAN",
    "WASHBURN",
    "WHEATON",
    "[EDIT]VILLAGES",
    "ARROW POINT",
    "BUTTERFIELD",
    "CHAIN-O-LAKES",
    "EMERALD BEACH",

    "EAGLE ROCK",
    "GOLDEN",
    "JENKINS",
    "PULASKIFIELD",
    "PLEASANT RIDGE",
    "SCHOLTEN",
    "SHELL KNOB",
    "VIOLA",
    "WHEELERVILLE",
    "YONKERVILLE",

    "ASH TWP",
    "BUTTERFIELD TWP",
    "CAPPS CREEK TWP",
    "CORSICANA TWP",
    "CRANE CREEK TWP",
    "EXETER TWP",
    "FLAT CREEK TWP",
    "JENKINS TWP",
    "KINGS PRAIRIE TWP",
    "LIBERTY TWP",
    "MCDONALD TWP",
    "MCDOWELL TWP",
    "MINERAL TWP",
    "MONETT TWP",
    "MOUNTAIN TWP",
    "OZARK TWP",
    "PIONEER TWP",
    "PLEASANT RIDGE TWP",
    "PURDY TWP",
    "ROARING RIVER TWP",
    "SHELL KNOB TWP",
    "SUGAR CREEK TWP",
    "WASHBURN TWP",
    "WHEATON TWP",
    "WHITE RIVER TWP",
    
    // Worth County
    "DENVER"
  };
}
