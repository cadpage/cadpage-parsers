package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYOntarioCountyParser extends GroupBestParser {
  
  public NYOntarioCountyParser() {
    super(new NYOntarioCountyAParser(),
           new NYOntarioCountyBParser());
  }
}
