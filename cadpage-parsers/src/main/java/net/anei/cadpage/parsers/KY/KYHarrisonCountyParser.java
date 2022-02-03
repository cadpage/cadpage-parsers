package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYHarrisonCountyParser extends GroupBestParser {

  public KYHarrisonCountyParser() {
    super(new KYHarrisonCountyAParser(), new KYHarrisonCountyBParser(),
          new KYHarrisonCountyCParser());
  }
}
