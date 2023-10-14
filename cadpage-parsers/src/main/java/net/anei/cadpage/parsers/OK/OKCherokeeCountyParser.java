package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Cherokee County, OK

 */
public class OKCherokeeCountyParser extends GroupBestParser {
  public OKCherokeeCountyParser() {
    super(new OKCherokeeCountyAParser(), new OKCherokeeCountyBParser());
  }
}
