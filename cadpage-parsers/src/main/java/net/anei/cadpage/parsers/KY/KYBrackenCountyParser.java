package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYBrackenCountyParser extends GroupBestParser {
  
  public KYBrackenCountyParser() {
    super(new KYBrackenCountyAParser(), new KYBrackenCountyBParser());
  }
}
