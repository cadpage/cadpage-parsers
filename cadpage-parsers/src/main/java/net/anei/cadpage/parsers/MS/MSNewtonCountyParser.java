package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSNewtonCountyParser extends GroupBestParser {

  public MSNewtonCountyParser() {
    super(new MSNewtonCountyAParser(), new MSNewtonCountyBParser());
  }
}
