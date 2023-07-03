package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;



public class WIKenoshaCountyParser extends GroupBestParser {

  public WIKenoshaCountyParser() {
    super(new WIKenoshaCountyAParser(),
           new WIKenoshaCountyBParser(),
           new WIKenoshaCountyCParser(),
           new WIKenoshaCountyDParser());
  }

  static final String[] CITY_LIST = new String[]{

      // City
      "KENOSHA",

      // Villages
      "BRISTOL",
      "GENOA CITY",
      "PADDOCK LAKE",
      "PLEASANT PRAIRIE",
      "SALEM LAKES",
      "SOMERS",
      "TWIN LAKES",

      // Towns
      "BRIGHTON",
      "PARIS",
      "RANDALL",
      "SOMERS",
      "WHEATLAND",

      // Census-designated places
      "CAMP LAKE",
      "LILY LAKE",
      "POWERS LAKE",
      "WILMOT",

      // Unincorporated communities

      "BASSETT",
      "BENET LAKE",
      "BERRYVILLE",
      "BRIGHTON",
      "CENTRAL PARK",
      "CHAPIN",
      "FOX RIVER",
      "KELLOGGS CORNERS",
      "KLONDIKE",
      "LAKE SHANGRILA",
      "LIBERTY CORNERS",
      "NEW MUNSTER",
      "PARIS",
      "SALEM OAKS",
      "SALEM",
      "TREVOR",
      "VOLTZ LAKE",

      // Ghost towns/neighborhoods
      "AURORA",

      // Mchenry county
      "HEBRON",

      // Waukesha County
      "EAGLE"
  };
}
