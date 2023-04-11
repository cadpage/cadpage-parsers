package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILWinnebagoCountyParser extends GroupBestParser {

  public ILWinnebagoCountyParser() {
    super(new ILWinnebagoCountyAParser(), new ILWinnebagoCountyBParser());

  }
}
