package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchInfoSysParser;



public class OHSummitCountyBParser extends DispatchInfoSysParser {
  
  public OHSummitCountyBParser() {
    this("SUMMIT COUNTY", "OH");
  }
  
  public OHSummitCountyBParser(String defCity, String defState) {
    super(OHSummitCountyParser.CITY_LIST, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "OHSummitCodeBParser";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PAREN_DIR_PTN.matcher(addr).replaceAll(" $2 $1 ");
    addr = CLEVE_MASS_PTN.matcher(addr).replaceAll("CLEVELAND MASSILON");
    return addr.trim().replaceAll("  +", " ");
  }
  private static final Pattern PAREN_DIR_PTN = Pattern.compile("(?<=^| )([^&\\d]*) \\(([NSEW])\\)");
  private static final Pattern CLEVE_MASS_PTN = Pattern.compile("\\bCleve-Mass\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    return OHSummitCountyParser.fixMapCity(city);
  }

}
