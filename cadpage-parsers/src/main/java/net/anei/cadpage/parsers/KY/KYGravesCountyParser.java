package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYGravesCountyParser extends GroupBestParser {
  
  public KYGravesCountyParser() {
    super(new KYGravesCountyAParser(), new KYGravesCountyBParser());
  }
}
