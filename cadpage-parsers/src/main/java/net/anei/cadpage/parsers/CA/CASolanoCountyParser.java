package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Solano County, CA
 */
public class CASolanoCountyParser extends GroupBestParser {
  public CASolanoCountyParser() {
    super(new CASolanoCountyAParser(), new CASolanoCountyBParser(),
          new CASolanoCountyCParser(), new CASolanoCountyDParser(),
          new CASolanoCountyEParser());
  }
}
