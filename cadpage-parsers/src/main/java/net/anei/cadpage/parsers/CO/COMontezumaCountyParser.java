package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Montezuma County, CO
 */
public class COMontezumaCountyParser extends GroupBestParser {
  

  public COMontezumaCountyParser() {
    super(new COMontezumaCountyAParser(), new COMontezumaCountyBParser());
   }
}
  





