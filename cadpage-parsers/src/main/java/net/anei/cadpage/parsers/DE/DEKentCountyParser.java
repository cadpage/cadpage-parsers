package net.anei.cadpage.parsers.DE;

import net.anei.cadpage.parsers.GroupBestParser;

public class DEKentCountyParser extends GroupBestParser {
  
  public DEKentCountyParser() {
    super(new DEKentCountyAParser(), new DEKentCountyBParser(),
          new DEKentCountyCParser(), new DEKentCountyDParser(),
          new DEKentCountyEParser(), new DEKentCountyFParser(), 
          new DEKentCountyGParser());
  }
}


