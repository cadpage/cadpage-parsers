package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MICassCountyParser extends GroupBestParser {
  
  public MICassCountyParser() {
    super(new MICassCountyAParser(), new MICassCountyBParser());
  }
} 