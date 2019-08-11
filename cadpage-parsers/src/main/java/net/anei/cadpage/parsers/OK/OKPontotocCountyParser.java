package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Pontotoc County, OK

 */
public class OKPontotocCountyParser extends GroupBestParser {
  public OKPontotocCountyParser() {
    super(new OKPontotocCountyAParser(), new OKPontotocCountyBParser());
  }
}
