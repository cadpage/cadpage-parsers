package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYGallatinCountyParser extends GroupBestParser {
  
  public KYGallatinCountyParser() {
    super(new KYGallatinCountyAParser(), new KYGallatinCountyBParser());
  }
}
