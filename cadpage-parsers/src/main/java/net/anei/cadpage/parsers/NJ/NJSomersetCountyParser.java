package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Somerset County, NJ
*/

public class NJSomersetCountyParser extends GroupBestParser {
  public NJSomersetCountyParser() {
    super(new NJSomersetCountyAParser(), new NJSomersetCountyBParser(), new NJSomersetCountyCParser());
  }
}
