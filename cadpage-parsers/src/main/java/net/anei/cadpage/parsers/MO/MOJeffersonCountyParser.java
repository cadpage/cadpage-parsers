package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOJeffersonCountyParser extends GroupBestParser {

  public MOJeffersonCountyParser() {
    super(new MOJeffersonCountyAParser(), new MOJeffersonCountyBParser());
  }
}
