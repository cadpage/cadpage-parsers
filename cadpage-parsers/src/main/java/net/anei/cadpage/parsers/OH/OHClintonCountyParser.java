package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Clinton County, OH
 */

public class OHClintonCountyParser extends GroupBestParser {

  public OHClintonCountyParser() {
    super(new OHClintonCountyAParser(), new OHClintonCountyBParser());
  }
}
