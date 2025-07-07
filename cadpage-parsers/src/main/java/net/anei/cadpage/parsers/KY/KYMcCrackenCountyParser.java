package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYMcCrackenCountyParser extends GroupBestParser {

  public KYMcCrackenCountyParser() {
    super(new KYMcCrackenCountyAParser(), new KYMcCrackenCountyBParser());
  }
}
