package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Morgan County, CO
 */
public class COMorganCountyParser extends GroupBestParser {


  public COMorganCountyParser() {
    super(new COMorganCountyAParser(), new COMorganCountyBParser());
   }
}






