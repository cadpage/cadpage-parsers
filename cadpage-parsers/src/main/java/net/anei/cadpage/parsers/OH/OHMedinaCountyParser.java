package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Medina County, OH
 */

public class OHMedinaCountyParser extends GroupBestParser {
  
  public OHMedinaCountyParser() {
    super(new OHMedinaCountyAParser(), 
          new OHMedinaCountyBParser(), 
          new OHMedinaCountyCParser(),
          new OHMedinaCountyDParser());
  }
}
