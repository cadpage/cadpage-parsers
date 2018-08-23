package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIIsabellaCountyParser extends GroupBestParser {
  
  public MIIsabellaCountyParser() {
    super(new MIIsabellaCountyAParser(), new MIIsabellaCountyBParser());
  }
} 