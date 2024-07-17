package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Essex County, NJ
*/


public class NJEssexCountyParser extends GroupBestParser {

  public NJEssexCountyParser() {
    super(new NJEssexCountyAParser(), new NJEssexCountyBParser());
  }
}
