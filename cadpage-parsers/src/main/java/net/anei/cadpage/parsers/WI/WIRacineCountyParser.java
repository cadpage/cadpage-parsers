package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIRacineCountyParser extends GroupBestParser {
  public WIRacineCountyParser() {
    super(new WIRacineCountyAParser(), new WIRacineCountyBParser());
  }
}
