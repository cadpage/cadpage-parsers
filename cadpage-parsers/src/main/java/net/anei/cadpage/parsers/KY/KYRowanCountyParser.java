package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYRowanCountyParser extends GroupBestParser {
  
  public KYRowanCountyParser() {
    super(new KYRowanCountyAParser(), new KYRowanCountyBParser());
  }
}
