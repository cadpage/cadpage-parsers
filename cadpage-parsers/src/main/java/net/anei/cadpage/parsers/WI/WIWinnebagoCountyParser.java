package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIWinnebagoCountyParser extends GroupBestParser {
  public WIWinnebagoCountyParser() {
    super(new WIWinnebagoCountyAParser(), 
          new WIWinnebagoCountyBParser(),
          new WIWinnebagoCountyCParser());
  }
}
