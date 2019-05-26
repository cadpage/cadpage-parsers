package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Lunenburg County, VA
 */
public class VALunenburgCountyParser extends GroupBestParser {
  
  public VALunenburgCountyParser() {
    super(new VALunenburgCountyAParser(), new VALunenburgCountyBParser());
  }
}
