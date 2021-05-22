package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * SanJoaquin County, CA
 */
public class CASanJoaquinCountyParser extends GroupBestParser {
  public CASanJoaquinCountyParser() {
    super(new CASanJoaquinCountyAParser(), new CASanJoaquinCountyBParser());
  }
}
