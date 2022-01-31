package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOHenryCountyParser extends GroupBestParser {

  public MOHenryCountyParser() {
    super(new MOHenryCountyAParser(), new MOHenryCountyBParser());
  }
}
