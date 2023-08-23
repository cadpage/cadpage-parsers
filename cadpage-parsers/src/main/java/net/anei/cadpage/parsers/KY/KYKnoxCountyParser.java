package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYKnoxCountyParser extends GroupBestParser {

  public KYKnoxCountyParser() {
    super(new KYKnoxCountyBParser(), new KYKnoxCountyCParser());
  }
}
