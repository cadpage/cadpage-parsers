package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * San Cruz County, CA
 */
public class CASantaCruzCountyParser extends GroupBestParser {
  public CASantaCruzCountyParser() {
    super(new CASantaCruzCountyAParser(), new CASantaCruzCountyBParser());
  }
}
