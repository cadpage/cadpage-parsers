package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIEatonCountyParser extends GroupBestParser {

  public MIEatonCountyParser() {
    super(new MIEatonCountyAParser(), new MIEatonCountyBParser());
  }
}