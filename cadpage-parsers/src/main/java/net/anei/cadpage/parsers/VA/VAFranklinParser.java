package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Franklin, VA
 */
public class VAFranklinParser extends GroupBestParser {
  
  public VAFranklinParser() {
    super(new VAFranklinAParser(), new VAFranklinBParser());
  }
}
