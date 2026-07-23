package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.GroupBestParser;


public class MTRavalliCountyParser extends GroupBestParser {

  public MTRavalliCountyParser() {
    super(new MTRavalliCountyAParser(),
          new MTRavalliCountyBParser());
  }
}
