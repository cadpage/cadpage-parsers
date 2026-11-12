package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOShelbyCountyParser extends GroupBestParser {

  public MOShelbyCountyParser() {
    super(new MOShelbyCountyAParser(), new MOShelbyCountyBParser());
  }
}
