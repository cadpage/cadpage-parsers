package net.anei.cadpage.parsers.WA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pierce County, WA
 */
public class WAPierceCountyParser extends GroupBestParser {

  public WAPierceCountyParser() {
    super(new WAPierceCountyAParser(), new WAPierceCountyBParser(),
          new WAPierceCountyCParser(), new WAPierceCountyEParser(),
          new WAPierceCountyFParser());
  }

  static String adjustMapAddressCommon(String sAddress) {
    sAddress = STATE_HWY_KP_PTN.matcher(sAddress).replaceAll("$1");
    StringBuffer sb = new StringBuffer();
    Matcher match = STREET_CODE_PTN.matcher(sAddress);
    while (match.find()) {
      match.appendReplacement(sb, convertCodes(match.group(), STREET_CODES));
    }
    match.appendTail(sb);
    return sb.toString();
  }
  private static final Pattern STATE_HWY_KP_PTN = Pattern.compile("\\b((?:ST|SR|WA) *\\d+) +K[NS]\\b");
  private static final Pattern STREET_CODE_PTN = Pattern.compile("\\b(?:AVCT|STCT|KN|KS)\\b");
  private static final Properties STREET_CODES = buildCodeTable(new String[]{
      "AVCT",   "AVE CT",
      "STCT",   "ST CT",
      "KN",     "KP N",
      "KS",     "KP S"
  });

  static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(new String[]{

      // Census Designated Communities
      "ALDER",
      "ALDERTON",
      "ANDERSON ISLAND",
      "AUBURN",
      "ARTONDALE",
      "ASHFORD",
      "BETHEL",
      "BONNEY LAKE",
      "BUCKLEY",
      "CARBONADO",
      "DUPONT",
      "EATONVILLE",
      "EDGEWOOD",
      "ELBE",
      "ELK PLAIN",
      "ENUMCLAW",
      "FIFE",
      "FIRCREST",
      "FORT LEWIS",
      "FOX ISLAND",
      "FREDERICKSON",
      "GIG HARBOR",
      "GRAHAM",
      "GREENWATER",
      "HOME",
      "KETRON ISLAND",
      "KEY CENTER",
      "LA GRANDE",
      "LAKEWOOD",
      "LONGBRANCH",
      "MCCHORD AFB",
      "MCKENNA",
      "MIDLAND",
      "MILTON",
      "ORTING",
      "PACIFIC",
      "PARKLAND",
      "PRAIRIE RIDGE",
      "PUYALLUP",
      "ROY",
      "RUSTON",
      "SOUTH HILL",
      "SOUTH PRAIRIE",
      "SPANAWAY",
      "STEILACOOM",
      "SUMMIT",
      "SUMNER",
      "TACOMA",
      "UNIVERSITY PLACE",
      "VAUGHN",
      "WALLER",
      "WAUNA",
      "WILKESON",
      "WOLLOCHET",

      // Other communities
      "AMERICAN LAKE",
      "ARLETTA",
      "BROWNS POINT",
      "BURNETT",
      "CASCADIA",
      "CEDARVIEW",
      "CRESCENT VALLEY",
      "CROCKER",
      "CROMWELL",
      "DASH POINT",
      "DIERINGER",
      "ELECTRON",
      "ELGIN",
      "FIRWOOD",
      "GLENCOVE",
      "HARBOR HEIGHTS",
      "HERRON ISLAND",
      "LAKEBAY",
      "LONGBRANCH",
      "LOVELAND",
      "MAPLEWOOD",
      "MCNEIL ISLAND",
      "MEEKER",
      "NATIONAL",
      "OAKBROOK",
      "OHOP",
      "PARADISE",
      "POINT FOSDICK",
      "PONDEROSA ESTATES",
      "RAFT ISLAND",
      "RHODODENDRON PARK",
      "ROSEDALE",
      "SHORE ACRES",
      "SHOREWOOD BEACH",
      "SUNNY BAY",
      "SUNRISE BEACH",
      "SYLVAN",
      "THRIFT",
      "TILLICUM",
      "VICTOR",
      "VILLA BEACH",
      "WARREN"
  }));

}
