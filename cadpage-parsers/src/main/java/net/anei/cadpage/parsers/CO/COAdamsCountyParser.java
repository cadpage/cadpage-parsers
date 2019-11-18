package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Adams County, CO
 */
public class COAdamsCountyParser extends GroupBestParser {
  

  public COAdamsCountyParser() {
    super(new COAdamsCountyAParser(), 
          new COAdamsCountyBParser(),
          new COAdamsCountyCParser());
   }
}
  





