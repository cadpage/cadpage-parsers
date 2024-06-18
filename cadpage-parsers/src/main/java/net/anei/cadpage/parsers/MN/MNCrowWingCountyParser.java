package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNCrowWingCountyParser extends GroupBestParser {

  public MNCrowWingCountyParser() {
    super(new MNCrowWingCountyAParser(), new MNCrowWingCountyBParser());
  }
}