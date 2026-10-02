package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOMontgomeryCountyParser extends GroupBestParser {

  public MOMontgomeryCountyParser() {
    super(new MOMontgomeryCountyBParser(), new MOMontgomeryCountyAParser());
  }
}
