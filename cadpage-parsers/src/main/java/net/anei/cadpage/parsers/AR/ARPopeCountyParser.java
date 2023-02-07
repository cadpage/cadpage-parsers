package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pope County, AR
 */
public class ARPopeCountyParser extends GroupBestParser {
  public ARPopeCountyParser() {
    super(new ARPopeCountyAParser(),
          new ARPopeCountyBParser());
  }
}
