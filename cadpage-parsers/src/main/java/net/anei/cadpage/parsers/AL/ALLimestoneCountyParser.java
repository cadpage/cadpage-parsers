package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALLimestoneCountyParser extends GroupBestParser {
  
  public ALLimestoneCountyParser() {
    super(new ALLimestoneCountyAParser(), new ALLimestoneCountyBParser());
  }
}
