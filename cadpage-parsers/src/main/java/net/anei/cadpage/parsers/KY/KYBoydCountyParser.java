package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYBoydCountyParser extends GroupBestParser {
  
  public KYBoydCountyParser() {
    super(new KYBoydCountyAParser(), new KYBoydCountyBParser());
  }
}
