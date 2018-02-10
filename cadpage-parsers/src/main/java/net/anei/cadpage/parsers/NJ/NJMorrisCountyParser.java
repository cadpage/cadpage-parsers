package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Morris County, NJ
*/


public class NJMorrisCountyParser extends GroupBestParser {
  
  public NJMorrisCountyParser() {
    super(new NJMorrisCountyAParser(), new NJMorrisCountyBParser(), 
          new NJMorrisCountyCParser(), new NJMorrisCountyDParser());
  }
}
