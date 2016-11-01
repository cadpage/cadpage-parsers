package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Mesa County, CO
 */
public class COMesaCountyParser extends GroupBestParser {
  

  public COMesaCountyParser() {
    super(new COMesaCountyAParser(), new COMesaCountyBParser());
   }
}
  





