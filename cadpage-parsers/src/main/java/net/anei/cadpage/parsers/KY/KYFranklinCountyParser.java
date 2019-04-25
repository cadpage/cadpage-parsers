package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYFranklinCountyParser extends GroupBestParser {
  
  public KYFranklinCountyParser() {
    super(new KYFranklinCountyAParser(), new KYFranklinCountyBParser());
  }
}
