package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYCarterCountyParser extends GroupBestParser {

  public KYCarterCountyParser() {
    super(new KYCarterCountyAParser(), new KYCarterCountyBParser());
  }
}
