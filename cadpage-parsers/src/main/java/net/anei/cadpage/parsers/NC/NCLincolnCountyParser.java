package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCLincolnCountyParser extends GroupBestParser {

  public NCLincolnCountyParser() {
    super(new NCLincolnCountyAParser(), new NCLincolnCountyBParser());
  }
}
