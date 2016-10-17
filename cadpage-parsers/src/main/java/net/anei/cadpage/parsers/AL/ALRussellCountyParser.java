package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALRussellCountyParser extends GroupBestParser {
  
  public ALRussellCountyParser() {
    super(new ALRussellCountyAParser(), new ALRussellCountyBParser());
  }
}
