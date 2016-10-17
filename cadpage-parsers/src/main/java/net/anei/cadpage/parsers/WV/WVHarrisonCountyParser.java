package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVHarrisonCountyParser extends GroupBestParser {
  
  public WVHarrisonCountyParser() {
    super(new WVHarrisonCountyAParser(), new WVHarrisonCountyBParser());
  }
}
