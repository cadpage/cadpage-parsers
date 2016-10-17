package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSDeSotoCountyParser extends GroupBestParser {

  @Override
  public String getLocName() {
    return "DeSoto County, MS";
  }

  public MSDeSotoCountyParser() {
    super(new MSDeSotoCountyAParser(), new MSDeSotoCountyBParser());
  }
}
