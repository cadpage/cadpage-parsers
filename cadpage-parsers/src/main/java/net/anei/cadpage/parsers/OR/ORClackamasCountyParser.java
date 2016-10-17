package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Washington County, OR
 * Also Clackamas County
 */
public class ORClackamasCountyParser extends GroupBestParser {
  public ORClackamasCountyParser() {
    super(new ORClackamasCountyAParser(), 
          new ORClackamasCountyBParser(),
          new ORClackamasCountyCParser(),
          new ORClackamasCountyDParser());
  }
  
  @Override
  public String getLocName() {
    return "Clackamas County, OR";
  }
}
