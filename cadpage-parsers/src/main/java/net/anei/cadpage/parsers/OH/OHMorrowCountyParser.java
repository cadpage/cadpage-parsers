package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
 * Morrow County, OH
 */

public class OHMorrowCountyParser extends GroupBestParser {

  public OHMorrowCountyParser() {
    super(new OHMorrowCountyAParser(), new OHMorrowCountyBParser());
  }

  private static final Pattern ZIP_CODE_PTN = Pattern.compile("\\d{5}");

  static void fixCity(Data data) {
    // Active911 added this zip code look up table for no apparent reason.
    // so we will do likewise
    if (ZIP_CODE_PTN.matcher(data.strCity).matches()) {
      String  city = ZIP_CODES.getProperty(data.strCity);
      if (city != null) data.strCity = city;
    }
  }

  private static final Pattern RD_NNN_PTN = Pattern.compile("\\bRD +(\\d{2,3})\\b");

  static String doAdjustMapAddress(String sAddress) {
    return RD_NNN_PTN.matcher(sAddress).replaceAll("TOWNSHIP RD $1");
  }

  private static final Properties ZIP_CODES = buildCodeTable(new String[] {
      "43003", "Ashley",
      "43011", "Centerburg",
      "43013", "Croton",
      "43015", "Delaware",
      "43016", "Dublin",
      "43017", "Dublin",
      "43021", "Galena",
      "43031", "Johnstown",
      "43035", "Lewis Center",
      "43040", "Marysville",
      "43054", "New Albany",
      "43061", "Ostrander",
      "43064", "Plain City",
      "43065", "Powell",
      "43066", "Radnor",
      "43074", "Sunbury",
      "43081", "Westerville",
      "43082", "Westerville",
      "43235", "Columbus",
      "43240", "Columbus",
      "43315", "Cardington",
      "43317", "Chesterville",
      "43321", "Fulton",
      "43334", "Marengo",
      "43338", "Mt. Gilead",
      "43342", "Prospect",
      "43344", "Richwood",
      "43350", "Sparta",
      "43356", "Waldo",
      "44315", "Cardington",
      "44833", "Galion"
  });

  static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1996 RD 146",                          "+40.435715,-83.010948",
      "77 RD 158",                            "+40.440529,-83.020379",
      "US RT 42 & RD 25",                     "+40.469301,-82.943640"
  });
}
