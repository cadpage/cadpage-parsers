package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Bergen County, NJ

*/

public class NJBergenCountyParser extends GroupBestParser {
  
  public NJBergenCountyParser() {
    super(new NJBergenCountyAParser(), new NJBergenCountyBParser(), new NJBergenCountyCParser());
  }
}
