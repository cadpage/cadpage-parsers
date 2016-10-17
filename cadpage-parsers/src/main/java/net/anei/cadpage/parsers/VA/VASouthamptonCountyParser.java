package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Southampton County, VA
 */
public class VASouthamptonCountyParser extends GroupBestParser {
  
  public VASouthamptonCountyParser() {
    super(new VASouthamptonCountyAParser(), new VASouthamptonCountyBParser());
  }
  
  static final String[] CITY_LIST = new String[]{
    // Towns
    "BOYKINS",
    "BRANCHVILLE",
    "CAPRON",
    "COURTLAND",
    "IVOR",
    "NEWSOMS",

    //Unincorporated communities
    "SEDLEY",
    "DREWRYVILLE",
    "BERLIN",
    "BLACK CREEK",
    
    "FRANKLIN",
    "WAKEFIELD",
    "WAVERLY"
  };
}
