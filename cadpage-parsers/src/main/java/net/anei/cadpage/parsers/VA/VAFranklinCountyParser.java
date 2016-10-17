package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Franklin County, VA
 */
public class VAFranklinCountyParser extends GroupBestParser {
  
  public VAFranklinCountyParser() {
    super(new VAFranklinCountyAParser(), new VAFranklinCountyBParser());
  }
}
