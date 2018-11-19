package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
 * Wayne County, OH
 */

public class OHWayneCountyParser extends GroupBestParser {
  
  public OHWayneCountyParser() {
    super(new OHWayneCountyBParser(),
          new OHWayneCountyCParser(),
          new OHWayneCountyDParser(),
          new OHWayneCountyEParser(),
          
          new GroupBlockParser(), new OHWayneCountyAParser());
  }
}
