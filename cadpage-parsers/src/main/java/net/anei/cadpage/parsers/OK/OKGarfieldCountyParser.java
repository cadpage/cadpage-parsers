package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Garfield County, OK

 */
public class OKGarfieldCountyParser extends GroupBestParser {
  public OKGarfieldCountyParser() {
    super(new OKGarfieldCountyAParser(), new OKGarfieldCountyBParser());
  }
}
