package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALLauderdaleCountyParser extends GroupBestParser {

  public ALLauderdaleCountyParser() {
    super(new ALLauderdaleCountyAParser(),
          new ALLauderdaleCountyBParser(),
          new ALLauderdaleCountyCParser());
  }
}
