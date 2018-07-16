package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIGratiotCountyParser extends GroupBestParser {
  
  public MIGratiotCountyParser() {
    super(new MIGratiotCountyAParser(), new MIGratiotCountyBParser());
  }
} 