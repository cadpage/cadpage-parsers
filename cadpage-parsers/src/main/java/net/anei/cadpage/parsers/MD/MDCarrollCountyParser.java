package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDCarrollCountyParser extends GroupBestParser {
  
  public MDCarrollCountyParser() {
    super(new MDCarrollCountyAParser(),
           new MDCarrollCountyBParser());
  }
  
}
