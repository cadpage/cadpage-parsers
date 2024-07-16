package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Greene County, OH
 */

public class OHGreeneCountyParser extends GroupBestParser {

  public OHGreeneCountyParser() {
    super(new OHGreeneCountyAParser(),
          new OHGreeneCountyBParser(),
          new OHGreeneCountyCParser());
  }
}
