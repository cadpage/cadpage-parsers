package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCSumterCountyParser extends GroupBestParser {
  
  public SCSumterCountyParser() {
    super(new SCSumterCountyAParser(), new SCSumterCountyBParser());
  }
}