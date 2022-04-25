package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIRockCountyParser extends GroupBestParser {
  public WIRockCountyParser() {
    super(new WIRockCountyAParser(), new WIRockCountyBParser());
  }
}
