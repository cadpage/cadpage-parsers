package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCDurhamCountyParser extends GroupBestParser {

  public NCDurhamCountyParser() {
    super(new NCDurhamCountyAParser(), new NCDurhamCountyBParser());
  }
}
