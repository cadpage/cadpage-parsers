package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOCapeGirardeauCountyParser extends GroupBestParser {

  public MOCapeGirardeauCountyParser() {
    super(new MOCapeGirardeauCountyAParser(), new MOCapeGirardeauCountyBParser());
  }
}
