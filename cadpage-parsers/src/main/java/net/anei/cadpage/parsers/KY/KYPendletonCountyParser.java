package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYPendletonCountyParser extends GroupBestParser {
  
  public KYPendletonCountyParser() {
    super(new KYPendletonCountyAParser(), new KYPendletonCountyBParser());
  }
}
