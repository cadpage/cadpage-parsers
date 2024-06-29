package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCSurryCountyParser extends GroupBestParser {

  public NCSurryCountyParser() {
    super(new NCSurryCountyAParser(), new NCSurryCountyBParser());
  }
}
