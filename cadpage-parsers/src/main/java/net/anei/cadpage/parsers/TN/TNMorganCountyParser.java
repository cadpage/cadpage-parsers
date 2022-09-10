package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNMorganCountyParser extends GroupBestParser {
  
  public TNMorganCountyParser() {
    super(new TNMorganCountyAParser(), new TNMorganCountyBParser(),
          new TNMorganCountyCParser());
  }
}
