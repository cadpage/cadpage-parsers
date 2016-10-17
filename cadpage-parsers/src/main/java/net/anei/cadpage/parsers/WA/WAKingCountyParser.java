package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * King County, WA
 */
public class WAKingCountyParser extends GroupBestParser {
  
  public WAKingCountyParser() {
    super(new WAKingCountyAParser(), new WAKingCountyBParser(), new WAKingCountyCParser());
  }
}
