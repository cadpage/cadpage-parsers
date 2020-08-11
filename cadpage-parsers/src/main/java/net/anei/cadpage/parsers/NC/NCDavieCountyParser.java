package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCDavieCountyParser extends GroupBestParser {

  public NCDavieCountyParser() {
    super(new NCDavieCountyAParser(), new NCDavieCountyBParser());
  }
}
