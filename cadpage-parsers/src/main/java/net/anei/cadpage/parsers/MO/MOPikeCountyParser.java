package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOPikeCountyParser extends GroupBestParser {

  public MOPikeCountyParser() {
    super(new MOPikeCountyAParser(), new MOPikeCountyBParser());
  }
}
