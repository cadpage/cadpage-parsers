package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Park County, CO
 */
public class COParkCountyParser extends GroupBestParser {


  public COParkCountyParser() {
    super(new COParkCountyAParser(), new COParkCountyBParser());
   }
}






