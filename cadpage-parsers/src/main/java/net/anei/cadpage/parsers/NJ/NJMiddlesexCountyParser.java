package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Middlesex County, NJ
*/


public class NJMiddlesexCountyParser extends GroupBestParser {

  public NJMiddlesexCountyParser() {
    super(new NJMiddlesexCountyAParser(),
          new NJMiddlesexCountyBParser(),
          new NJMiddlesexCountyEParser(),
          new NJMiddlesexCountyFParser());
  }
}
