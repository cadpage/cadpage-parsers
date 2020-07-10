package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVMasonCountyParser extends GroupBestParser {

  public WVMasonCountyParser() {
    super(new WVMasonCountyAParser(), new WVMasonCountyBParser());
  }
}
