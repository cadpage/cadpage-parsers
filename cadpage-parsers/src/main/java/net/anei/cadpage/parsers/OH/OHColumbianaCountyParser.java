package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
 * Columbiana County, OH
 */

public class OHColumbianaCountyParser extends GroupBestParser {
  public OHColumbianaCountyParser() {
    super(new OHColumbianaCountyBParser(),
          new OHColumbianaCountyCParser(),
          new GroupBlockParser(),
          new OHColumbianaCountyAParser());
  }
}
