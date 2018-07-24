package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INJacksonCountyParser extends GroupBestParser {
  
  public INJacksonCountyParser() {
    super(new INJacksonCountyAParser(), new INJacksonCountyBParser());
  }

}
