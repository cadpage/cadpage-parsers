package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Mercer County, NJ

*/

public class NJMercerCountyParser extends GroupBestParser {
  
  public NJMercerCountyParser() {
    super(new NJMercerCountyAParser(), new NJMercerCountyBParser());
  }
}
