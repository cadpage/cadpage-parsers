package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAPikeCountyParser extends GroupBestParser {
  
  public PAPikeCountyParser() {
    super(new PAPikeCountyAParser(), 
          new PAPikeCountyBParser());
  }
  
}