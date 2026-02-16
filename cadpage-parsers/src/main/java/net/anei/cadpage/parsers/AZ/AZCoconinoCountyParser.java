package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Coconino County, AZ
 */
public class AZCoconinoCountyParser extends GroupBestParser {
  public AZCoconinoCountyParser() {
    super(new AZCoconinoCountyAParser(), new AZCoconinoCountyBParser());
  }
}
