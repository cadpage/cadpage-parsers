package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Douglas County, CO
 */
public class CODouglasCountyParser extends GroupBestParser {
  

  public CODouglasCountyParser() {
    super(new CODouglasCountyAParser(), new CODouglasCountyBParser());
   }
}
  





