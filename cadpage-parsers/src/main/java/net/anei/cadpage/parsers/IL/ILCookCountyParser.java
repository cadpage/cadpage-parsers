package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILCookCountyParser extends GroupBestParser {
  
  public ILCookCountyParser() {
    super(new ILCookCountyAParser(), new ILCookCountyBParser(), new ILCookCountyCParser(), 
        new ILCookCountyDParser(), new ILCookCountyEParser(), new ILCookCountyFParser(),
        new ILCookCountyGParser(), new ILCookCountyHParser());

  }
}
