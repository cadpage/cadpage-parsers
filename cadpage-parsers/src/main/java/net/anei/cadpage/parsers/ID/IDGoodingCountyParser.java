package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDGoodingCountyParser extends GroupBestParser {
  
  public IDGoodingCountyParser() {
   super(new IDGoodingCountyAParser(), new IDGoodingCountyBParser());
  }
}
