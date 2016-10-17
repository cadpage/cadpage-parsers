package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIOutagamieCountyParser extends GroupBestParser {
  public WIOutagamieCountyParser() {
    super(new WIOutagamieCountyAParser(), new WIOutagamieCountyBParser());
  }
}
