package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNFranklinCountyParser extends GroupBestParser {

  public TNFranklinCountyParser() {
    super(new TNFranklinCountyAParser(), new TNFranklinCountyBParser());
  }
}
