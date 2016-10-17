package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYOldhamCountyParser extends GroupBestParser {
  
  public KYOldhamCountyParser() {
    super(new KYOldhamCountyAParser(), new KYOldhamCountyBParser());
  }
}
