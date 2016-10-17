package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Burlington County, NJ
*/

public class NJBurlingtonCountyParser extends GroupBestParser {
  
  public NJBurlingtonCountyParser() {
    super(new NJBurlingtonCountyAParser(), 
           new NJBurlingtonCountyBParser(),
           new NJBurlingtonCountyCParser(),
           new NJBurlingtonCountyDParser(),
           new NJBurlingtonCountyEParser(),
           new NJBurlingtonCountyFParser(),
           new NJBurlingtonCountyGParser());
  }
}
