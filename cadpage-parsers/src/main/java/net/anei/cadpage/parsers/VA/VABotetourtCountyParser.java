package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Botetourt County, VA
 */
public class VABotetourtCountyParser extends GroupBestParser {

  public VABotetourtCountyParser() {
    super(new VABotetourtCountyAParser(), new VABotetourtCountyBParser());
  }
}
