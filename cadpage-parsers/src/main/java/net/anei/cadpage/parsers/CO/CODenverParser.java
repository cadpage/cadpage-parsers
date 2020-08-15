package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Denver, CO
 */
public class CODenverParser extends GroupBestParser {


  public CODenverParser() {
    super(new CODenverAParser(), new CODenverBParser());
   }
}