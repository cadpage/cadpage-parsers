package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSOttawaCountyParser extends GroupBestParser {

  public KSOttawaCountyParser() {
    super(new KSOttawaCountyAParser(), new KSOttawaCountyBParser());
  }

}
