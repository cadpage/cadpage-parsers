package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/**
* Benton County, OR
 */

public class ORBentonCountyParser extends GroupBestParser {
  public ORBentonCountyParser() {
    super(new ORBentonCountyBParser(), new GroupBlockParser(), new ORBentonCountyCParser());
  }
}
