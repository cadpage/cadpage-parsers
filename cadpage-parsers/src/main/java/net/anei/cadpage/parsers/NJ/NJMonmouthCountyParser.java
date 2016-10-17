package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Monmouth County, NJ

*/

public class NJMonmouthCountyParser extends GroupBestParser {
  
  public NJMonmouthCountyParser() {
    super(new NJMonmouthCountyAParser(), new NJMonmouthCountyBParser());
  }
}
