package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/**
 * Pittsylvania County, VA
 */
public class VAPittsylvaniaCountyParser extends GroupBestParser {

  public VAPittsylvaniaCountyParser() {
    super(new VAPittsylvaniaCountyBParser(),
          new GroupBlockParser(),
          new VAPittsylvaniaCountyAParser());
  }
}
