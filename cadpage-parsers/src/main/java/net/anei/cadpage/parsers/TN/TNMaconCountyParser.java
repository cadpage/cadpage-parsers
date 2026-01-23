package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNMaconCountyParser extends GroupBestParser {

  public TNMaconCountyParser() {
    super(new TNMaconCountyAParser(), new TNMaconCountyBParser());
  }
}
