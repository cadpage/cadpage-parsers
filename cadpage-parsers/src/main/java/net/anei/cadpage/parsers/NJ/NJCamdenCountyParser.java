package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Camden County, NJ

*/

public class NJCamdenCountyParser extends GroupBestParser {
  
  public NJCamdenCountyParser() {
    super(new NJCamdenCountyAParser(), new NJCamdenCountyBParser());
  }
}
