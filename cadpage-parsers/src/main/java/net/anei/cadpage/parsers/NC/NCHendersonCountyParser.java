package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCHendersonCountyParser extends GroupBestParser {

  public NCHendersonCountyParser() {
    super(new NCHendersonCountyAParser(), new NCHendersonCountyBParser());
  }
}
