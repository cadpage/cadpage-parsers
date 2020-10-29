package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCHokeCountyParser extends GroupBestParser {

  public NCHokeCountyParser() {
    super(new NCHokeCountyAParser(), new NCHokeCountyBParser());
  }
}
