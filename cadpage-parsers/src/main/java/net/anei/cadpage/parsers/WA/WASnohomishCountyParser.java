package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Snohomish County, WA
 */
public class WASnohomishCountyParser extends GroupBestParser {

  public WASnohomishCountyParser() {
    super(new WASnohomishCountyAParser(), new WASnohomishCountyBParser(),
          new WASnohomishCountyCParser(), new WASnohomishCountyDParser(),
          new WASnohomishCountyEParser(), new WASnohomishCountyFParser(),
          new WASnohomishCountyGParser());
  }
}
