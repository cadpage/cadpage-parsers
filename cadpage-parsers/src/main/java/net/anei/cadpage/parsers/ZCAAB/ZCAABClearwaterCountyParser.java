package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * ClearwaterCounty, AB
 */
public class ZCAABClearwaterCountyParser extends GroupBestParser {

  public ZCAABClearwaterCountyParser() {
    super(new ZCAABClearwaterCountyAParser(), new ZCAABClearwaterCountyBParser());
  }
}
