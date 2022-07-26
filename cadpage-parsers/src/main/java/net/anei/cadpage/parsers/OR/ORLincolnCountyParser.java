package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Lincoln County, OR
 */

public class ORLincolnCountyParser extends GroupBestParser {
  public ORLincolnCountyParser() {
    super(new ORLincolnCountyAParser(), new ORLincolnCountyBParser());
  }
}
