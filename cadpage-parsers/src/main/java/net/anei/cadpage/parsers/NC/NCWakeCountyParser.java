package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCWakeCountyParser extends GroupBestParser {

  public NCWakeCountyParser() {
    super(new NCWakeCountyAParser(),
          new NCWakeCountyBParser(),
          new NCWakeCountyCParser(),
          new NCWakeCountyDParser(),
          new NCWakeCountyEParser(),
          new NCWakeCountyFParser());
  }

  @Override
  public String getLocName() {
    return "Wake County, NC";
  }

}
