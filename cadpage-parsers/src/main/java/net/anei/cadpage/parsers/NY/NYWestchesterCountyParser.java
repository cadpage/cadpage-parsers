package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYWestchesterCountyParser extends GroupBestParser {

  public NYWestchesterCountyParser() {
    super(new NYWestchesterCountyAParser(),
          new NYWestchesterCountyBParser());
  }
}
