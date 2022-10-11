package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYOhioCountyParser extends GroupBestParser {

  public KYOhioCountyParser() {
    super(new KYOhioCountyAParser(), new KYOhioCountyBParser());
  }
}
