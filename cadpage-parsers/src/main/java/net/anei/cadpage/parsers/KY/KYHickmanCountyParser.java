package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYHickmanCountyParser extends GroupBestParser {

  public KYHickmanCountyParser() {
    super(new KYHickmanCountyAParser(), new KYHickmanCountyBParser());
  }
}
