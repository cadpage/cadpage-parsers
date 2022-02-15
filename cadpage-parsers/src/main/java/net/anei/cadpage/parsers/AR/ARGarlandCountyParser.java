package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Garland County, AR
 */
public class ARGarlandCountyParser extends GroupBestParser {
  public ARGarlandCountyParser() {
    super(new ARGarlandCountyAParser(),
          new ARGarlandCountyCParser(),
          new ARGarlandCountyDParser());
  }
}
