package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Delores County, CO
 */
public class CODeloresCountyParser extends GroupBestParser {


  public CODeloresCountyParser() {
    super(new CODeloresCountyAParser(), new CODeloresCountyBParser());
   }
}