package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOPittisCountyParser extends GroupBestParser {

  public MOPittisCountyParser() {
    super(new MOPikeCountyAParser(), new MOPikeCountyBParser());
  }
}
