package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Chaffee County, CO
 */
public class COChaffeeCountyParser extends GroupBestParser {


  public COChaffeeCountyParser() {
    super(new COChaffeeCountyAParser(), new COChaffeeCountyBParser());
   }
}