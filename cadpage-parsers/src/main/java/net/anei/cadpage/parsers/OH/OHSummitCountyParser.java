package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Summit County, OH
 */

public class OHSummitCountyParser extends GroupBestParser {
  
  public OHSummitCountyParser() {
    super(new OHSummitCountyAParser(),
           new OHSummitCountyBParser(),
           new OHSummitCountyCParser(),
           new OHSummitCountyDParser(),
           new OHSummitCountyEParser(),
           new OHSummitCountyFParser(),
           new OHSummitCountyGParser(),
           new OHHudsonParser());
  }
  
  
  @Override
  public String getLocName() {
    return "Summit County, OH";
  }


  static final String[] CITY_LIST = new String[]{
    
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
    "BOSTON HTS",
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
    "BATH TWP",
    "BOSTON TWP",
    "COPLEY TWP",
    "COVENTRY TWP",
    "NORTHFIELD CENTER TWP",
    "RICHFIELD TWP",
    "SAGAMORE HILLS TWP",
    "SPRINGFIELD TWP",
    "TWINSBURG TWP",
    "BATH TNSP",
    "BOSTON TNSP",
    "COPLEY TNSP",
    "COVENTRY TNSP",
    "NORTHFIELD CENTER TNSP",
    "RICHFIELD TNSP",
    "SAGAMORE HILLS TNSP",
    "SPRINGFIELD TNSP",
    "TWINSBURG TNSP",
    
    
    // Medina County
    "WADSWORTH",
    
    // Portage County
    "AURORA",
    "STREETSBORO"
  };
}
