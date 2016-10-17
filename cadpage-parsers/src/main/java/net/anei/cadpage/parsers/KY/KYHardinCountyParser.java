package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYHardinCountyParser extends GroupBestParser {
  
  public KYHardinCountyParser() {
    super(new KYHardinCountyAParser(), new KYHardinCountyBParser(), new KYHardinCountyCParser());
  }
}
