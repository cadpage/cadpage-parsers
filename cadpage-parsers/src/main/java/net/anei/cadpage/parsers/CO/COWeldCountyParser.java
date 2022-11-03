package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Weld County, CO
 */
public class COWeldCountyParser extends GroupBestParser {


  public COWeldCountyParser() {
    super(new COWeldCountyAParser(), new COWeldCountyBParser());
  }
}






