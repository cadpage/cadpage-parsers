package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIBayCountyParser extends GroupBestParser {
  
  public MIBayCountyParser() {
    super(new MIBayCountyAParser(), new MIBayCountyBParser());
  }
} 