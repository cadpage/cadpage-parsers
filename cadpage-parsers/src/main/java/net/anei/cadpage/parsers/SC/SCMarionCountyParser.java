package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCMarionCountyParser extends GroupBestParser {
  
  public SCMarionCountyParser() {
    super(new SCMarionCountyAParser(), new SCMarionCountyBParser());
  }
}