package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVLincolnCountyParser extends GroupBestParser {

  public WVLincolnCountyParser() {
    super(new WVLincolnCountyAParser(), new WVLincolnCountyBParser());
  }
}
