package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSLauderdaleCountyParser extends GroupBestParser {

  public MSLauderdaleCountyParser() {
    super(new MSLauderdaleCountyAParser(), new MSLauderdaleCountyBParser());
  }
}
