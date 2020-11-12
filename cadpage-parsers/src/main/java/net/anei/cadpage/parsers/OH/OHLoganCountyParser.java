package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Logan County, OH
 */

public class OHLoganCountyParser extends GroupBestParser {

  public OHLoganCountyParser() {
    super(new OHLoganCountyBParser(),
          new OHLoganCountyCParser(),
          new OHLoganCountyDParser());
  }
}
