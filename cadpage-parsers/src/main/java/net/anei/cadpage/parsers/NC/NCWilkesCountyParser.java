package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCWilkesCountyParser extends GroupBestParser {

  public NCWilkesCountyParser() {
    super(new NCWilkesCountyAParser(),
          new NCWilkesCountyBParser());
  }
}
