package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Sebastian County, AR
 */
public class ARSebastianCountyParser extends GroupBestParser {
  public ARSebastianCountyParser() {
    super(new ARSebastianCountyAParser(),
          new ARSebastianCountyBParser());
  }
}
