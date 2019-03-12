package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INBooneCountyParser extends GroupBestParser {
  
  public INBooneCountyParser() {
    super(new INBooneCountyAParser(),
          new INBooneCountyBParser(),
          new INBooneCountyCParser());
  }

}
