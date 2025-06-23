package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYMetcalfeCountyParser extends GroupBestParser {

  public KYMetcalfeCountyParser() {
    super(new KYMetcalfeCountyAParser(), new KYMetcalfeCountyBParser());
  }
}
