package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Humboldt County, CA
 */
public class CAHumboldtCountyParser extends GroupBestParser {
  public CAHumboldtCountyParser() {
    super(new CAHumboldtCountyAParser(), new CAHumboldtCountyBParser());
  }
}
