package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVWyomingCountyParser extends GroupBestParser {

  public WVWyomingCountyParser() {
    super(new WVWyomingCountyAParser(), new WVWyomingCountyBParser());
  }
}
