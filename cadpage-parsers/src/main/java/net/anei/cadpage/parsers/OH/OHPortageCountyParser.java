package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
 * Portage County, OH
 */

public class OHPortageCountyParser extends GroupBestParser {

  public OHPortageCountyParser() {
    super(new OHPortageCountyAParser(), new OHPortageCountyBParser(), new OHPortageCountyCParser(),
        new OHPortageCountyDParser(), new OHPortageCountyFParser(),
        new GroupBlockParser(), new OHPortageCountyCencommParser());
  }

  static String fixMapAddress(String addr) {
    return STATE225_PTN.matcher(addr).replaceAll("STATE ROUTE 225");
  }
  private static final Pattern STATE225_PTN = Pattern.compile("\\b(STATE|ST|OH) +225\\b", Pattern.CASE_INSENSITIVE);


  static String fixCity(String city) {
    String fixCity = FIX_CITY_TABLE.getProperty(city.toUpperCase());
    return fixCity != null ? fixCity : city;
  }

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "EDINBERT",     "EDINBURG",
      "EDIN",         "EDINBURG",
      "GARRETSVILE",  "GARRETTSVILLE",
      "ROOTSOWN",     "ROOTSTOWN",
  });

  static final String[] CITY_LIST = new String[]{

    // Townships
    "ATWATER TOWNSHIP",
    "BRIMFIELD TOWNSHIP",
    "CHARLESTOWN TOWNSHIP",
    "DEERFIELD TOWNSHIP",
    "EDINBURG TOWNSHIP",
    "FRANKLIN TOWNSHIP",
    "FREEDOM TOWNSHIP",
    "HIRAM TOWNSHIP",
    "MANTUA TOWNSHIP",
    "NELSON TOWNSHIP",
    "PALMYRA TOWNSHIP",
    "PARIS TOWNSHIP",
    "RANDOLPH TOWNSHIP",
    "RAVENNA TOWNSHIP",
    "ROOTSTOWN TOWNSHIP",
    "SHALERSVILLE TOWNSHIP",
    "SUFFIELD TOWNSHIP",
    "WINDHAM TOWNSHIP",

    "ATWATER TWP",
    "BRIMFIELD TWP",
    "CHARLESTOWN TWP",
    "DEERFIELD TWP",
    "EDINBURG TWP",
    "FRANKLIN TWP",
    "FREEDOM TWP",
    "HIRAM TWP",
    "MANTUA TWP",
    "NELSON TWP",
    "PALMYRA TWP",
    "PARIS TWP",
    "RANDOLPH TWP",
    "RAVENNA TWP",
    "ROOTSTOWN TWP",
    "SHALERSVILLE TWP",
    "SUFFIELD TWP",
    "WINDHAM TWP",

    // Townships without suffix
    "ATWATER",
    "BRIMFIELD",
    "CHARLESTOWN",
    "DEERFIELD",
    "EDINBURG",
    "EDINBERG",    // Misspelled
    "EDIN",        // ditto
    "FRANKLIN",
    "FREEDOM",
    "NELSON",
    "PALMYRA",
    "PARIS",
    "RANDOLPH",
    "ROOTSTOWN",
    "ROOTSOWN",   // Misspelled
    "SHALERSVILLE",
    "SUFFIELD",

    // Cities and villages
    "AURORA",
    "BRADY LAKE",
    "GARRETTSVILLE",
    "GARRETSVILE",  // Misspelled
    "HIRAM",
    "KENT",
    "MANTUA",
    "MOGADORE",
    "RAVENNA",
    "STREETSBORO",
    "SUGAR BUSH KNOLLS",
    "WINDHAM",

    // Unincorporated communities
    "DIAMOND",
    "WAYLAND",

    // Census-designated places
    "ATWATER",
    "BRIMFIELD",

    // Geauga County
    "AUBURN",
    "AUBURN TWP",
    "BURTON",
    "TROY",
    "TROY TWP",

    // Mahoning County
    "BERLIN",
    "BERLIN CENTER",

    // Trumbull County
    "NEWTON FALLS",
    "SOUTHINGTON TWP"
  };

}
