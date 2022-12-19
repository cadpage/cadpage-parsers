package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCFairfieldCountyParser extends GroupBestParser {

  public SCFairfieldCountyParser() {
    super(new SCFairfieldCountyAParser(), new SCFairfieldCountyBParser());
  }
}