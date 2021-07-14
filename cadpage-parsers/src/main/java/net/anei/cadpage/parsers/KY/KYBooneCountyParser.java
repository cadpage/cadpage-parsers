package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYBooneCountyParser extends GroupBestParser {

  public KYBooneCountyParser() {
    super(new KYBooneCountyAParser(), new KYBooneCountyBParser(),
          new KYBooneCountyCParser());
  }
}
