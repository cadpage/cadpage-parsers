package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Campbell County, VA
 */
public class VACampbellCountyParser extends GroupBestParser {

  public VACampbellCountyParser() {
    super(new VACampbellCountyAParser(),
          new VACampbellCountyBParser(),
          new VACampbellCountyCParser());
  }
}
