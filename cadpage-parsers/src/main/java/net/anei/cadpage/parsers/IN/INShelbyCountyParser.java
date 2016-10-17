package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INShelbyCountyParser extends GroupBestParser {
  
  public INShelbyCountyParser() {
    super(new INShelbyCountyAParser(), new INShelbyCountyBParser());
  }

}
