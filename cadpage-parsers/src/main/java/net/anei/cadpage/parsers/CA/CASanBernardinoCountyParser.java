package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * San Bernardino County, CA
 */
public class CASanBernardinoCountyParser extends GroupBestParser {
  public CASanBernardinoCountyParser() {
    super(new CASanBernardinoCountyAParser(), new CASanBernardinoCountyBParser(),
          new CASanBernardinoCountyCParser(), new CASanBernardinoCountyDParser(),
          new CASanBernardinoCountyEParser());
  }
}
