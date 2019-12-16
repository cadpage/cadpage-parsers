package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYTrimbleCountyParser extends GroupBestParser {
  
  public KYTrimbleCountyParser() {
    super(new KYTrimbleCountyAParser(), new KYTrimbleCountyBParser());
  }
}
