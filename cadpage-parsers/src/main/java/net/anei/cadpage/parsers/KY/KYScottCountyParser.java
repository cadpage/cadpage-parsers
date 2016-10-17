package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYScottCountyParser extends GroupBestParser {
  
  public KYScottCountyParser() {
    super(new KYScottCountyAParser(), new KYScottCountyBParser());
  }
}
