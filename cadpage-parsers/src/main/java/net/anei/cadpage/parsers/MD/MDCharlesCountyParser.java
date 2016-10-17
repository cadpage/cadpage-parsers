package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDCharlesCountyParser extends GroupBestParser {
  
  public MDCharlesCountyParser() {
    super(new MDCharlesCountyAParser(),
           new MDCharlesCountyBParser(),
           new MDCharlesCountyCParser());
  }
  
}
