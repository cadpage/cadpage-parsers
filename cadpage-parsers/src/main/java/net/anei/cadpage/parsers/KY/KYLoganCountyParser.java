package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYLoganCountyParser extends GroupBestParser {
  
  public KYLoganCountyParser() {
    super(new KYLoganCountyAParser(), new KYLoganCountyBParser());
  }
}
