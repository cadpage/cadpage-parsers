package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCAlleghanyCountyParser extends GroupBestParser {

  public NCAlleghanyCountyParser() {
    super(new NCAlleghanyCountyAParser(), new NCAlleghanyCountyBParser());
  }
}
