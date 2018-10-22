package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INMarionCountyParser extends GroupBestParser {
  
  public INMarionCountyParser() {
    super(new INMarionCountyAParser(), new INMarionCountyBParser());
  }

}
