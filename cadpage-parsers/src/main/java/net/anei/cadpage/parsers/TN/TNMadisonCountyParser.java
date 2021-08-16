package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNMadisonCountyParser extends GroupBestParser {

  public TNMadisonCountyParser() {
    super(new TNMadisonCountyAParser(), new TNMadisonCountyBParser());
  }
}
