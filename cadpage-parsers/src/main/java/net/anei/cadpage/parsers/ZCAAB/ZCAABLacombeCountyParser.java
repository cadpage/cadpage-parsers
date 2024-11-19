package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Lacombe County, AB
 */
public class ZCAABLacombeCountyParser extends GroupBestParser {

  public ZCAABLacombeCountyParser() {
    super(new ZCAABLacombeCountyAParser(), new ZCAABLacombeCountyBParser());
  }
}
