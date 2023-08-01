package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Jackson County, OH
 */

public class OHJacksonCountyParser extends GroupBestParser {

  public OHJacksonCountyParser() {
    super(new OHJacksonCountyAParser(), new OHJacksonCountyBParser());
  }
}
