package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * San Luis Obispo County, CA
 */
public class CASanLuisObispoCountyParser extends GroupBestParser {
  public CASanLuisObispoCountyParser() {
    super(new CASanLuisObispoCountyAParser(),
          new CASanLuisObispoCountyBParser(),
          new CASanLuisObispoCountyCParser());
  }
}
