package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIBarryCountyParser extends GroupBestParser {
  
  public MIBarryCountyParser() {
    super(new MIBarryCountyAParser(), new MIBarryCountyBParser());
  }
} 