package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Trumbull County, OH
 */

public class OHTrumbullCountyParser extends GroupBestParser {

  public OHTrumbullCountyParser() {
    super(new OHTrumbullCountyAParser(),
           new OHTrumbullCountyBParser());
  }
}
