package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Clear Creek County, CO
 */
public class COClearCreekCountyParser extends GroupBestParser {


  public COClearCreekCountyParser() {
    super(new COClearCreekCountyAParser(),
          new COClearCreekCountyBParser(),
          new COClearCreekCountyCParser());
   }
}






