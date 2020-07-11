package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYTriggCountyParser extends GroupBestParser {

  public KYTriggCountyParser() {
    super(new KYTriggCountyAParser(), new KYTriggCountyBParser());
  }
}
