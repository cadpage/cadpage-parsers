package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNCockeCountyParser extends GroupBestParser {

  public TNCockeCountyParser() {
    super(new TNCockeCountyAParser(), new TNCockeCountyBParser());
  }
}
