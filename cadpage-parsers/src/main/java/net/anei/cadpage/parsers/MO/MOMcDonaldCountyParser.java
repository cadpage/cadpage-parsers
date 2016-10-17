package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOMcDonaldCountyParser extends GroupBestParser {

  public MOMcDonaldCountyParser() {
    super(new MOMcDonaldCountyAParser(), new MOMcDonaldCountyBParser());
  }
}
