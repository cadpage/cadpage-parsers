package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIOttawaCountyParser extends GroupBestParser {
  
  public MIOttawaCountyParser() {
    super(new MIOttawaCountyAParser(), new MIOttawaCountyBParser());
  }
} 