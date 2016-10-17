package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Cleveland County, OK

 */
public class OKClevelandCountyParser extends GroupBestParser {
  public OKClevelandCountyParser() {
    super(new OKClevelandCountyAParser(), new OKClevelandCountyBParser());
  }
}
