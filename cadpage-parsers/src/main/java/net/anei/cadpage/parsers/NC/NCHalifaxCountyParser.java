package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCHalifaxCountyParser extends GroupBestParser {

  public NCHalifaxCountyParser() {
    super(new NCHalifaxCountyAParser(), new NCHalifaxCountyBParser());
  }
}
