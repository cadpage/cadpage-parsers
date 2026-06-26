package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Bryan County, OK

 */
public class OKBryanCountyParser extends GroupBestParser {
  public OKBryanCountyParser() {
    super(new OKBryanCountyAParser(), new OKBryanCountyBParser());
  }
}
