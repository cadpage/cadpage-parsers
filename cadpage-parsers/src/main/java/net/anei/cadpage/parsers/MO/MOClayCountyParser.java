package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOClayCountyParser extends GroupBestParser {

  public MOClayCountyParser() {
    super(new MOClayCountyAParser(), new MOClayCountyCParser());
  }
}
