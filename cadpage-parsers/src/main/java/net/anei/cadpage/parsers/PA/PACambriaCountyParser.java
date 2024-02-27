package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Cambria County, PA
 */


public class PACambriaCountyParser extends GroupBestParser {

  public PACambriaCountyParser() {
    super(new PACambriaCountyAParser(), new PACambriaCountyBParser());
  }
}
