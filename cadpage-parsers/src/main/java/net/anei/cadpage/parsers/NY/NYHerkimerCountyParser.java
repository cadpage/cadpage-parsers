package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYHerkimerCountyParser extends GroupBestParser {
  
  public NYHerkimerCountyParser() {
    super(new NYHerkimerCountyAParser(), new NYHerkimerCountyBParser());
  }
}
