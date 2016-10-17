package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchInfoSysParser;



public class OHSummitCountyBParser extends DispatchInfoSysParser {
  
  public OHSummitCountyBParser() {
    this("SUMMIT COUNTY", "OH");
  }
  
  public OHSummitCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
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
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "AKRON",
    "BARBERTON",
    "CAYAHOGA FALLS",
    "FAIRLAWN",
    "GREEN",
    "HUDSON",
    "MACEDONIA",
    "MONROE FALLS",
    "NEW FRANKLIN",
    "NORTON",
    "STOW",
    "TALLMADGE",
    "TWINSBURG",
    
    // Villages
    "BOSTON HEIGHTS",
    "CLINTON",
    "LAKEMORE",
    "MOGADORE",
    "NORTHFIELD",
    "PENINSULA",
    "REMINDERVILLE",
    "RICHFIELD",
    "SILVER LAKE",
    
    // Townships
    "BATH",
    "BOSTON",
    "COPLEY",
    "COVENTRY",
    "NORTHFIELD CENTER",
    "RICHFIELD",
    "SAGAMORE HILLS",
    "SPRINGFIELD",
    "TWINSBURG",
    
    
    // Medina County
    "WADSWORTH",
    "WADSWORTH TOWNSHIP",
    "WADSWORTH TWP"
    
  };
}
