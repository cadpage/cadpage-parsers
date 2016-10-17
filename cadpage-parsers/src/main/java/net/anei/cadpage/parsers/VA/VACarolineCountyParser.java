package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Caroline County, VA
 */
public class VACarolineCountyParser extends GroupBestParser {
  
  public VACarolineCountyParser() {
    super(new VACarolineCountyAParser(), new VACarolineCountyBParser());
  }
}
