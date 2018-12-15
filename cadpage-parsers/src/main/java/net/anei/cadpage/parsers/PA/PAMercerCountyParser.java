package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Mercer County, PA
 */


public class PAMercerCountyParser extends GroupBestParser {
  
  public PAMercerCountyParser() {
    super(new PAMercerCountyAParser(),
          new PAMercerCountyBParser());
  }
}
