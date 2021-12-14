package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCAbbevilleCountyParser extends GroupBestParser {

  public SCAbbevilleCountyParser() {
    super(new SCAbbevilleCountyAParser(), new SCAbbevilleCountyBParser());
  }
}
