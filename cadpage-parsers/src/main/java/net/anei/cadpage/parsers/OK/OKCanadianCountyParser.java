package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Canadian County, OK

 */
public class OKCanadianCountyParser extends GroupBestParser {
  public OKCanadianCountyParser() {
    super(new OKCanadianCountyAParser(), new OKCanadianCountyBParser());
  }
}
