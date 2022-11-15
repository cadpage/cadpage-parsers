package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Dooly County, GA
 */
public class GADoolyCountyParser extends GroupBestParser {

  public GADoolyCountyParser() {
    super(new GADoolyCountyAParser(), new GADoolyCountyBParser());
  }
}
