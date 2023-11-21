package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOOsageCountyParser extends GroupBestParser {

  public MOOsageCountyParser() {
    super(new MOOsageCountyAParser(), new MOOsageCountyBParser());
  }
}
