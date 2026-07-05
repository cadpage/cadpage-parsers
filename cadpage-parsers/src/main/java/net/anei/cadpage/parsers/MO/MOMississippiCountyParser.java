package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOMississippiCountyParser extends GroupBestParser {

  public MOMississippiCountyParser() {
    super(new MOMississippiCountyAParser(), new MOMississippiCountyBParser());
  }
}
