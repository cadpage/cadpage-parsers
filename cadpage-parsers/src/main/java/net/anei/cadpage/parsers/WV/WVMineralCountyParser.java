package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVMineralCountyParser extends GroupBestParser {
  
  public WVMineralCountyParser() {
    super(new WVMineralCountyAParser(), new WVMineralCountyBParser());
  }
}
