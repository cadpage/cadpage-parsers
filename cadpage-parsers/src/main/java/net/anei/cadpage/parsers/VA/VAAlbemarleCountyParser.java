package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Albemarle County, VA
 */
public class VAAlbemarleCountyParser extends GroupBestParser {
  
  public VAAlbemarleCountyParser() {
    super(new VAAlbemarleCountyAParser(), new VAAlbemarleCountyBParser(),
        new VAAlbemarleCountyCParser());
  }
}
