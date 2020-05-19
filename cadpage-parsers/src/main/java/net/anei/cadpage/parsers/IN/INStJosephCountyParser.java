package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INStJosephCountyParser extends GroupBestParser {
  
  public INStJosephCountyParser() {
    super(new INStJosephCountyAParser(), 
          new INStJosephCountyBParser(),
          new INStJosephCountyCParser());
  }

}
