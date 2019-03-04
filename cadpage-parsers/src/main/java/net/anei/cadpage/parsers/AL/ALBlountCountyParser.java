package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALBlountCountyParser extends GroupBestParser {
  
  public ALBlountCountyParser() {
    super(new ALBlountCountyAParser(), new ALBlountCountyBParser());
  }
}
