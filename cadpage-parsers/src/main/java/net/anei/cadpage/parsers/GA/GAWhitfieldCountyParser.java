package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Whitfield County, GA
 */
public class GAWhitfieldCountyParser extends GroupBestParser {
  
  public GAWhitfieldCountyParser() {
    super(new GAWhitfieldCountyAParser(), new GAWhitfieldCountyBParser(), new GAWhitfieldCountyCParser());
  }
}
