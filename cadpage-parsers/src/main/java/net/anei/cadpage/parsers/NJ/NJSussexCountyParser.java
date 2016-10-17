package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Sussex County, NJ 

 */


public class NJSussexCountyParser extends GroupBestParser {
  
  public NJSussexCountyParser() {
    super(new NJSussexCountyAParser(), 
        new NJSussexCountyBParser(), 
        new NJSussexCountyCParser(), 
        new NJSussexCountyDParser());
  }
}

