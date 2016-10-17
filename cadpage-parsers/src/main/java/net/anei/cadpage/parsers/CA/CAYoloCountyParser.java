package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Yolo County, CA
 */
public class CAYoloCountyParser extends GroupBestParser {
  public CAYoloCountyParser() {
    super(new CAYoloCountyAParser(), new CAYoloCountyBParser());
  }
}
