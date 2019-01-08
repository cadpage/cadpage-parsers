package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSCrawfordCountyParser extends GroupBestParser {
  
  public KSCrawfordCountyParser() {
    super(new KSCrawfordCountyAParser(), new KSCrawfordCountyBParser());
  }

}
