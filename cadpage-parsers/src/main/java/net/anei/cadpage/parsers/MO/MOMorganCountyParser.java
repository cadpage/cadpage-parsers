package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOMorganCountyParser extends GroupBestParser {

  public MOMorganCountyParser() {
    super(new MOMorganCountyBParser(), new MOMorganCountyAParser());
  }
}
