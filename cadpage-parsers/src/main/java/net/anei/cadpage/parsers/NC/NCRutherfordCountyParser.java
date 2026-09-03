package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCRutherfordCountyParser extends GroupBestParser {

  public NCRutherfordCountyParser() {
    super(new NCRutherfordCountyAParser(), new NCRutherfordCountyBParser());
  }
}
