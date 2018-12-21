package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Champaign County, OH
 */

public class OHChampaignCountyParser extends GroupBestParser {
  
  public OHChampaignCountyParser() {
    super(new OHChampaignCountyAParser(), new OHChampaignCountyBParser());
  }
}
