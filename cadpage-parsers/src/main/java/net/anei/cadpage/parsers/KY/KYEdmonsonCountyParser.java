package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYEdmonsonCountyParser extends GroupBestParser {

  public KYEdmonsonCountyParser() {
    super(new KYEdmonsonCountyAParser(), new KYEdmonsonCountyBParser());
  }
}
