package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCGreenvilleCountyParser extends GroupBestParser {
  
  public SCGreenvilleCountyParser() {
    super(new SCGreenvilleCountyAParser(), new SCGreenvilleCountyBParser());
  }
}