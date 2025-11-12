package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSJacksonCountyParser extends GroupBestParser {
  
  public KSJacksonCountyParser() {
    super(new KSJacksonCountyAParser(), new KSJacksonCountyBParser());
  }

}
