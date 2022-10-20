package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Jefferson County, WA
 */
public class WAJeffersonCountyParser extends GroupBestParser {

  public WAJeffersonCountyParser() {
    super(new WAJeffersonCountyAParser(), new WAJeffersonCountyBParser(),
          new WAJeffersonCountyCParser());
  }
}
