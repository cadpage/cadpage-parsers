package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Suisun City, CA
 */
public class CASuisunCityParser extends GroupBestParser {
  public CASuisunCityParser() {
    super(new CASuisunCityAParser(), new CASuisunCityBParser());
  }
}
