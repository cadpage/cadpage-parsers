package net.anei.cadpage.parsers.IA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Dickinson County, IA
 */
public class IADickinsonCountyParser extends DispatchEmergitechParser {
  
  public IADickinsonCountyParser() {
    super("DC911:", true, CITY_LIST, "DICKINSON COUNTY", "IA", TrailAddrType.PLACE_INFO);
    setupSpecialStreets("AVE A", "AVE B", "AVE C", "AVE D", "AVE E", "AVE F",
                        "LAKE PARK CARE",
                        "LAKE PARK CARE CENTER");
  }
  
  public String getFilter() {
    return "DC911@secureserver.net";
  }
  
  private static final Pattern DIR_AVE_X_PTN = Pattern.compile("\\b([NSEW]) (AVE [A-F])\\b");
  @Override
  public String adjustMapAddress(String addr) {
    addr = DIR_AVE_X_PTN.matcher(addr).replaceAll("$2 $1");
    return super.adjustMapAddress(addr);
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Incorporated Cities
    "ARNOLDS PARK",
    "LAKE PARK",
    "MILFORD",
    "OKOBOJI",
    "ORLEANS",
    "SPIRIT LAKE",
    "SUPERIOR",
    "TERRIL",
    "WAHPETON",
    "WEST OKOBOJI",
    
    // Unincorporated Communities
    "MONTGOMERY",
    
    // Townships
    "CENTER GROVE TWP",
    "DIAMOND LAKE TWP",
    "EXCELSIOR TWP",
    "LAKEVILLE TWP",
    "LLOYD TWP",
    "MILFORD TWP",
    "OKOBOJI TWP",
    "RICHLAND TWP",
    "SILVER LAKE TWP",
    "SPIRIT LAKE TWP",
    "SUPERIOR TWP",
    "WESTPORT TWP"
  };
}
