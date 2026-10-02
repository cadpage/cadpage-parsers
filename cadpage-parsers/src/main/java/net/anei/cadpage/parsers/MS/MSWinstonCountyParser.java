package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSWinstonCountyParser extends GroupBestParser {

  public MSWinstonCountyParser() {
    super(new MSWinstonCountyAParser(), new MSWinstonCountyBParser());
  }
}
