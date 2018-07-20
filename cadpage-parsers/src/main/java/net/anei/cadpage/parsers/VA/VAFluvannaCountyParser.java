package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Fluvanna County, VA
 */
public class VAFluvannaCountyParser extends GroupBestParser {
  
  public VAFluvannaCountyParser() {
    super(new VAFluvannaCountyAParser(), new VAFluvannaCountyBParser(), new VAFluvannaCountyCParser());
  }
}
