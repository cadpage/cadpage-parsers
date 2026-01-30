package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Rio Grande County, CO
 */
public class CORioGrandeCountyParser extends GroupBestParser {


  public CORioGrandeCountyParser() {
    super(new CORioGrandeCountyAParser(),
          new CORioGrandeCountyBParser(),
          new CORioGrandeCountyCParser());
   }
}






