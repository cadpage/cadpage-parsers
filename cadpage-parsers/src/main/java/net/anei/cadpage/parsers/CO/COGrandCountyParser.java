package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Grand County, CO
 */
public class COGrandCountyParser extends GroupBestParser {


  public COGrandCountyParser() {
    super(new COGrandCountyAParser(), new COGrandCountyBParser());
  }
}






