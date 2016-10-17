package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INVigoCountyParser extends GroupBestParser {
  
  public INVigoCountyParser() {
    super(new INVigoCountyAParser(), new INVigoCountyBParser());
  }

}
