package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSNeshobaCountyParser extends GroupBestParser {

  public MSNeshobaCountyParser() {
    super(new MSNeshobaCountyAParser(), new MSNeshobaCountyBParser());
  }
}
