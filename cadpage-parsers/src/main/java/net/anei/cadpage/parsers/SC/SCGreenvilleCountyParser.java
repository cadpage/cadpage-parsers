package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class SCGreenvilleCountyParser extends GroupBestParser {
  
  public SCGreenvilleCountyParser() {
    super(new SCGreenvilleCountyAParser(), 
          new SCGreenvilleCountyBParser(),
          new SCGreenvilleCountyDParser());
  }
}