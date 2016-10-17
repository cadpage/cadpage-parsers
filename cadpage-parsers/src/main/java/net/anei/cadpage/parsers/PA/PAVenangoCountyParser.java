package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAVenangoCountyParser extends GroupBestParser {
  
  public PAVenangoCountyParser() {
    super(new PAVenangoCountyAParser(), 
          new PAVenangoCountyBParser());
  }
  
}