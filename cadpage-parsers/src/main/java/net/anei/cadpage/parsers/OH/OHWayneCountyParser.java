package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Wayne County, OH
 */

public class OHWayneCountyParser extends GroupBestParser {
  
  public OHWayneCountyParser() {
    super(new OHWayneCountyAParser(),
          new OHWayneCountyBParser(),
          new OHWayneCountyCParser(),
          new OHWayneCountyDParser());
  }
}
