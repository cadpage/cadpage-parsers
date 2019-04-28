package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PASnyderCountyParser extends GroupBestParser {
  
  public PASnyderCountyParser() {
    super(new PASnyderCountyAParser(), 
          new PASnyderCountyBParser());
  }
  
}