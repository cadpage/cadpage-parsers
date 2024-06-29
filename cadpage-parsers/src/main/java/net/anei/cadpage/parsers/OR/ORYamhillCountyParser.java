package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Yamhill County, OR
 */
public class ORYamhillCountyParser extends GroupBestParser {
  public ORYamhillCountyParser() {
    super(new ORYamhillCountyAParser(),
          new ORYamhillCountyCParser());
  }
}
