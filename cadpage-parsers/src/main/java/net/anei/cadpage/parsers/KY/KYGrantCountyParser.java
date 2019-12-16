package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYGrantCountyParser extends GroupBestParser {
  
  public KYGrantCountyParser() {
    super(new KYGrantCountyAParser(), new KYGrantCountyBParser());
  }
}
