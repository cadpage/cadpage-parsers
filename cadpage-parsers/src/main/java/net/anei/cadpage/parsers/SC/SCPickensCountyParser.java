package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCPickensCountyParser extends GroupBestParser {
  
  public SCPickensCountyParser() {
    super(new SCPickensCountyAParser(), new SCPickensCountyBParser());
  }
}