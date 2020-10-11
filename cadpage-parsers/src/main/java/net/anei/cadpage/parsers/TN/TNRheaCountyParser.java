package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNRheaCountyParser extends GroupBestParser {
  
  public TNRheaCountyParser() {
    super(new TNRheaCountyAParser(), new TNRheaCountyBParser(), new TNRheaCountyCParser());
  }
}
