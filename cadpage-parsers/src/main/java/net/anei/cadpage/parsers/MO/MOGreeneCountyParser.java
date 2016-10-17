package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOGreeneCountyParser extends GroupBestParser {

  public MOGreeneCountyParser() {
    super(new MOGreeneCountyAParser(), new MOGreeneCountyBParser());
  }
}
