package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.GroupBestParser;

public class WYSubletteCountyParser extends GroupBestParser {
  public WYSubletteCountyParser() {
    super(new WYSubletteCountyAParser(), new WYSubletteCountyBParser());
  }
}
