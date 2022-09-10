package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYLewisCountyParser extends GroupBestParser {

  public NYLewisCountyParser() {
    super(new NYLewisCountyAParser(), new NYLewisCountyBParser());
  }
}
