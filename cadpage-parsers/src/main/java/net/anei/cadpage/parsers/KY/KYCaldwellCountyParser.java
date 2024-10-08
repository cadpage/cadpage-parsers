package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYCaldwellCountyParser extends GroupBestParser {

  public KYCaldwellCountyParser() {
    super(new KYCaldwellCountyAParser(), new KYCaldwellCountyBParser());
  }
}
