package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Jackson County, GA
 */
public class GAJacksonCountyParser extends GroupBestParser {

  public GAJacksonCountyParser() {
    super(new GAJacksonCountyAParser(), new GAJacksonCountyBParser());
  }
}
