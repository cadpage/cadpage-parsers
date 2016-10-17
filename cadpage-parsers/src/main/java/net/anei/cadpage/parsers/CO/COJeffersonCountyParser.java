package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Jefferson County, CO
 */
public class COJeffersonCountyParser extends GroupBestParser {
  

  public COJeffersonCountyParser() {
    super(new COJeffersonCountyAParser(), new COJeffersonCountyBParser());
   }
}
  





