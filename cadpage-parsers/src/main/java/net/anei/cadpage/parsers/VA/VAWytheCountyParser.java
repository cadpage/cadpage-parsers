package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Wythe County, VA
 */
public class VAWytheCountyParser extends GroupBestParser {

  public VAWytheCountyParser() {
    super(new VAWytheCountyAParser(), new VAWytheCountyBParser(),
          new VAWytheCountyCParser());
  }
}
