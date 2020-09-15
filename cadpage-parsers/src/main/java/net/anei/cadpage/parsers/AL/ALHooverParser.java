package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALHooverParser extends GroupBestParser {
  
  public ALHooverParser() {
    super(new ALHooverAParser(), new ALHooverBParser());
  }
}
