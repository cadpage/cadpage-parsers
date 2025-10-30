package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.GroupBestParser;


public class LAJeffersonParishParser extends GroupBestParser {
  
  public LAJeffersonParishParser() {
    super(new LAJeffersonParishAParser(), 
          new LAJeffersonParishBParser());
  }
}
