package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVCabellCountyParser extends GroupBestParser {

  public WVCabellCountyParser() {
    super(new WVCabellCountyAParser(), new WVCabellCountyBParser());
  }
}
