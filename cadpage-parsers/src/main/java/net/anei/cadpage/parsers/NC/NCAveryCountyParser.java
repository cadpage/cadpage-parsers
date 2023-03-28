package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCAveryCountyParser extends GroupBestParser {

  public NCAveryCountyParser() {
    super(new NCAveryCountyAParser(), new NCAveryCountyBParser());
  }
}
