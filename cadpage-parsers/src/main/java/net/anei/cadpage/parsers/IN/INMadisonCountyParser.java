package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INMadisonCountyParser extends GroupBestParser {
  
  public INMadisonCountyParser() {
    super(new INMadisonCountyAParser(), 
          new INMadisonCountyBParser(), 
          new INMadisonCountyCParser(),
          new INMadisonCountyEParser());
  }

}
