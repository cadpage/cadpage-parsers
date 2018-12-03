package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVGrantCountyParser extends GroupBestParser {
  
  public WVGrantCountyParser() {
    super(new WVGrantCountyAParser(), new WVGrantCountyBParser());
  }
}
