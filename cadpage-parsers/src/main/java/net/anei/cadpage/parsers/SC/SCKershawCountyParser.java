package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCKershawCountyParser extends GroupBestParser {

  public SCKershawCountyParser() {
    super(new SCKershawCountyAParser(), new SCKershawCountyBParser());
  }
}