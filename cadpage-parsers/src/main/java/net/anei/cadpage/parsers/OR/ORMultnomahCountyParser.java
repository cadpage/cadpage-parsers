package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Multnomah County, OR
 */
public class ORMultnomahCountyParser extends GroupBestParser {
  public ORMultnomahCountyParser() {
    super(new ORMultnomahCountyAParser(),
          new ORMultnomahCountyBParser(),
          new ORMultnomahCountyCParser(),
          new ORMultnomahCountyDParser(),
          new ORMultnomahCountyEParser());
  }
}
