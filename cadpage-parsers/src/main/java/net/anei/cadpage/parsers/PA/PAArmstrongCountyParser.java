package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Armstrong County, PA
 */


public class PAArmstrongCountyParser extends GroupBestParser {

  public PAArmstrongCountyParser() {
    super(new PAArmstrongCountyBParser(),
          new PAArmstrongCountyCParser());
  }
}
