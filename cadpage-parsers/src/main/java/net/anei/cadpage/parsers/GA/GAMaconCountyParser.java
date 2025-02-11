package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Macon County, GA
 */
public class GAMaconCountyParser extends GroupBestParser {

  public GAMaconCountyParser() {
    super(new GAMaconCountyAParser(),
          new GAMaconCountyBParser(),
          new GAMaconCountyCParser());
  }
}
