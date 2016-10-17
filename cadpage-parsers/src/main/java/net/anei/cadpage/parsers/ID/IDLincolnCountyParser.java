package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDLincolnCountyParser extends GroupBestParser {
  
  public IDLincolnCountyParser() {
   super(new IDLincolnCountyAParser(), new IDLincolnCountyBParser());
  }
}
