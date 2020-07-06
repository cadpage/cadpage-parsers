package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSNeoshoCountyParser extends GroupBestParser {

  public KSNeoshoCountyParser() {
    super(new KSNeoshoCountyAParser(), new KSNeoshoCountyBParser());
  }

}
