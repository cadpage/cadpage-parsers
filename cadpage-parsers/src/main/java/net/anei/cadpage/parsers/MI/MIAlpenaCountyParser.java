package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIAlpenaCountyParser extends GroupBestParser {

  public MIAlpenaCountyParser() {
    super(new MIAlpenaCountyAParser(), new MIAlpenaCountyBParser());
  }
}