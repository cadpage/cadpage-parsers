package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Monroe County, NY 

 */

public class NYMonroeCountyParser extends GroupBestParser {
  
  public NYMonroeCountyParser() {
    super(new NYMonroeCountyRuralMetroParser(), new NYMonroeCountyWebsterParser());
  }
}
