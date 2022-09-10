package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIKentCountyParser extends GroupBestParser {

  public MIKentCountyParser() {
    super(new MIKentCountyAParser(), new MIKentCountyBParser());
  }
}
