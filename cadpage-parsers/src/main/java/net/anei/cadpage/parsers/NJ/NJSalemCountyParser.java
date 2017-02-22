package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Salem County, NJ

*/

public class NJSalemCountyParser extends GroupBestParser {
  
  public NJSalemCountyParser() {
    super(new NJSalemCountyAParser(),
          new NJSalemCountyBParser(),
          new NJSalemCountyCParser());
  }
}
