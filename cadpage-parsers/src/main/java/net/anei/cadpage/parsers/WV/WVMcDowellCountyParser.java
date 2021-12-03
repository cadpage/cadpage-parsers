package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVMcDowellCountyParser extends GroupBestParser {

  public WVMcDowellCountyParser() {
    super(new WVMcDowellCountyAParser(), new WVMcDowellCountyBParser());
  }
}
