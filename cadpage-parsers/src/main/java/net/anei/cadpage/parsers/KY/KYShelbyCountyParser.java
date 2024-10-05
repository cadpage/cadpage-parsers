package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYShelbyCountyParser extends GroupBestParser {

  public KYShelbyCountyParser() {
    super(new KYShelbyCountyAParser(), new KYShelbyCountyBParser());
  }
}
