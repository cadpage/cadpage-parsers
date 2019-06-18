package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDCarolineCountyParser extends GroupBestParser {
  
  public MDCarolineCountyParser() {
    super(new MDCarolineCountyAParser(),
           new MDCarolineCountyBParser());
  }
  
}
