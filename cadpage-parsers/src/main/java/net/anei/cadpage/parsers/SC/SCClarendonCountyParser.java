package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCClarendonCountyParser extends GroupBestParser {
  
  public SCClarendonCountyParser() {
    super(new SCClarendonCountyAParser(), new SCClarendonCountyBParser());
  }
}