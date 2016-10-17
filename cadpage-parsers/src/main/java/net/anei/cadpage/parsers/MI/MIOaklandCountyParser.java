package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIOaklandCountyParser extends GroupBestParser {
  
  public MIOaklandCountyParser() {
    super(new MIOaklandCountyAParser(), new MIOaklandCountyBParser(),
        new MIOaklandCountyCParser(), new MIOaklandCountyDParser());
  }
} 