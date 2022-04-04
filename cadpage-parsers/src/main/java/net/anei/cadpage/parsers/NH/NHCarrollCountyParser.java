package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Carroll County, NH
*/


public class NHCarrollCountyParser extends GroupBestParser {

  public NHCarrollCountyParser() {
    super(new NHCarrollCountyAParser(), new NHCarrollCountyBParser());
  }
}
