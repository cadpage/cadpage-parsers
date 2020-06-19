package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOLincolnCountyParser extends GroupBestParser {

  public MOLincolnCountyParser() {
    super(new MOLincolnCountyAParser(), new MOLincolnCountyBParser());
  }
}
