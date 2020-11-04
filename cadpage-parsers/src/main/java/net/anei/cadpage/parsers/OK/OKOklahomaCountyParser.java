package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Oklahoma County, OK

 */
public class OKOklahomaCountyParser extends GroupBestParser {
  public OKOklahomaCountyParser() {
    super(new OKOklahomaCountyAParser(), new OKOklahomaCountyBParser());
  }
}
