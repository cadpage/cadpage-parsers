package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYLivingstonCountyParser extends GroupBestParser {
  
  public NYLivingstonCountyParser() {
    super(new NYLivingstonCountyAParser(),
           new NYLivingstonCountyBParser(),
           new NYLivingstonCountyCParser());
  }
}
