package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILMadisonCountyParser extends GroupBestParser {
  
  public ILMadisonCountyParser() {
    super(new ILMadisonCountyAParser(), 
          new ILMadisonCountyBParser(),
          new ILMadisonCountyCParser());

  }
}
