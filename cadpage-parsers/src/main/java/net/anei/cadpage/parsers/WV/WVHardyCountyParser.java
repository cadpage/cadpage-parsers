package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;



public class WVHardyCountyParser extends GroupBestParser {
  
  public WVHardyCountyParser() {
    super(new WVHardyCountyBParser(),
          new WVHardyCountyCParser());
  }
}
