package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSMiamiCountyParser extends GroupBestParser {

  public KSMiamiCountyParser() {
    super(new KSMiamiCountyAParser(), new KSMiamiCountyBParser());
  }

}
