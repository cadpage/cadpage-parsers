package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Tulare, CA
 */
public class CATulareCountyParser extends GroupBestParser {
  public CATulareCountyParser() {
    super(new CATulareCountyAParser(),
          new CATulareCountyBParser(),
          new CATulareCountyCParser());
  }
}
