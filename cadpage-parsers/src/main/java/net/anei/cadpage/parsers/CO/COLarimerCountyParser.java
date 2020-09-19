package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Larimer County, CO
 */
public class COLarimerCountyParser extends GroupBestParser {
  

  public COLarimerCountyParser() {
    super(new COLarimerCountyAParser(), new COLarimerCountyBParser(), 
          new COLarimerCountyCParser(), new COLarimerCountyDParser(),
          new COLarimerCountyEParser(), new COLarimerCountyFParser());
   }
}
  





