package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Henry County, GA
 */
public class GAHenryCountyParser extends GroupBestParser {
  
  public GAHenryCountyParser() {
    super(new GAHenryCountyAParser(), new GAHenryCountyBParser());
  }
}
