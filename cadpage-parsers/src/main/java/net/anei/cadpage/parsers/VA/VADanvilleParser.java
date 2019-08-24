package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Danville, VA
 */
public class VADanvilleParser extends GroupBestParser {
  
  public VADanvilleParser() {
    super(new VADanvilleAParser(), new VADanvilleBParser());
  }
}
