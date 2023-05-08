package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Shasta County, CA
 */
public class CAShastaCountyParser extends GroupBestParser {
  public CAShastaCountyParser() {
    super(new CAShastaCountyAParser(), new CAShastaCountyBParser(),
          new CAShastaCountyCParser(), new CAShastaCountyDParser(),
          new CAShastaCountyEParser());
  }
}
