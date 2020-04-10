package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Henry County, VA
 */
public class VAHenryCountyParser extends GroupBestParser {
  
  public VAHenryCountyParser() {
    super(new VAHenryCountyAParser(), new VAHenryCountyBParser());
  }
}
