package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYFloydCountyParser extends GroupBestParser {

  public KYFloydCountyParser() {
    super(new KYFloydCountyAParser(), new KYFloydCountyBParser());
  }
}
