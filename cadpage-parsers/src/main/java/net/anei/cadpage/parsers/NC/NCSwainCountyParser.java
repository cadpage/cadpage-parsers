package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCSwainCountyParser extends GroupBestParser {

  public NCSwainCountyParser() {
    super(new NCSwainCountyBParser(),
          new NCSwainCountyAParser());
  }
}
