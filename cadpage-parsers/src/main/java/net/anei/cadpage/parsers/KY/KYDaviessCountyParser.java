package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYDaviessCountyParser extends GroupBestParser {

  public KYDaviessCountyParser() {
    super(new KYDaviessCountyAParser(), new KYDaviessCountyBParser());
  }
}
