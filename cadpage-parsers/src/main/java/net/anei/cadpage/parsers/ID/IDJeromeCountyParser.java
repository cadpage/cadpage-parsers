package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDJeromeCountyParser extends GroupBestParser {
  
  public IDJeromeCountyParser() {
   super(new IDJeromeCountyAParser(), new IDJeromeCountyBParser());
  }
}
