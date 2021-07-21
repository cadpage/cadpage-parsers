package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVMorganCountyParser extends GroupBestParser {

  public WVMorganCountyParser() {
    super(new WVMorganCountyAParser(),
          new WVMorganCountyBParser(),
          new WVMorganCountyCParser());
  }
}
