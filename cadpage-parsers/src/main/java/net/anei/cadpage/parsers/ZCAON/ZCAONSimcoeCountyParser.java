package net.anei.cadpage.parsers.ZCAON;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Simcoe County, ON
 */


public class ZCAONSimcoeCountyParser extends GroupBestParser {

  public ZCAONSimcoeCountyParser() {
    super(new ZCAONSimcoeCountyAParser(),
          new ZCAONSimcoeCountyBParser(),
          new ZCAONSimcoeCountyCParser());
  }
}
