package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCMaconCountyParser extends GroupBestParser {
  
  public NCMaconCountyParser() {
    super(new NCMaconCountyAParser(), new NCMaconCountyBParser());
  }
}
