package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDShoshoneCountyParser extends GroupBestParser {
  
  public IDShoshoneCountyParser() {
   super(new IDShoshoneCountyAParser(), new IDShoshoneCountyBParser());
  }
}
