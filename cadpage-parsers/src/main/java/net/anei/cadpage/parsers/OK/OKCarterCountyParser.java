package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Carter County, OK

 */
public class OKCarterCountyParser extends GroupBestParser {
  public OKCarterCountyParser() {
    super(new OKCarterCountyAParser(), new OKCarterCountyBParser());
  }
}
