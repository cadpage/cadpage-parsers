package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Canmore, AB
 */
public class ZCAABCanmoreParser extends GroupBestParser {
  
  public ZCAABCanmoreParser() {
    super(new ZCAABCanmoreAParser(), new ZCAABCanmoreBParser());
  }
}
