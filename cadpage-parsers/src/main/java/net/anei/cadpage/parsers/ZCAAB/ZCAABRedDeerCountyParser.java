package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Red Deer County, AB
 */
public class ZCAABRedDeerCountyParser extends GroupBestParser {

  public ZCAABRedDeerCountyParser() {
    super(new ZCAABRedDeerCountyAParser(), new ZCAABRedDeerCountyBParser());
  }
}
