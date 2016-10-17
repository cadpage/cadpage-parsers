package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCWakeCountyParser extends GroupBestParser {
  
  public NCWakeCountyParser() {
    super(new NCWakeCountyAParser(), new NCWakeCountyBParser());
  }

  @Override
  public String getLocName() {
    return "Wake County, NC";
  }
  
}
