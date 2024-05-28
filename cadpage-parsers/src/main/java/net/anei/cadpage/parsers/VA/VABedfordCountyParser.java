package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Bedford County, VA
 */
public class VABedfordCountyParser extends GroupBestParser {

  public VABedfordCountyParser() {
    super(new VABedfordCountyAParser(),
          new VABedfordCountyBParser(),
          new VABedfordCountyCParser());
  }
}
