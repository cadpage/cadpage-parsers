package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Boulder County, CO
 */
public class COBoulderCountyParser extends GroupBestParser {


  public COBoulderCountyParser() {
    super(new COBoulderCountyAParser(),
          new COBoulderCountyBParser(),
          new COBoulderCountyCParser());
   }
}






