package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Placer County, CA
 */
public class CAPlacerCountyParser extends GroupBestParser {
  public CAPlacerCountyParser() {
    super(new CAPlacerCountyAParser(),
          new CAPlacerCountyBParser(),
          new CAPlacerCountyCParser(),
          new CAPlacerCountyDParser());
  }
}
