package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Cumberland County, NJ
*/


public class NJCumberlandCountyParser extends GroupBestParser {
  
  public NJCumberlandCountyParser() {
    super(new NJCumberlandCountyAParser(), new NJCumberlandCountyBParser());
  }
}
