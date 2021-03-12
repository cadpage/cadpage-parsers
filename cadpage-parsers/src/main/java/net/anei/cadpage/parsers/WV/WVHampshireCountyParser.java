package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVHampshireCountyParser extends GroupBestParser {

  public WVHampshireCountyParser() {
    super(new WVHampshireCountyAParser(), new WVHampshireCountyBParser());
  }
}
