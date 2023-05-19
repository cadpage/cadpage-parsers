package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALCoosaCountyParser extends GroupBestParser {

  public ALCoosaCountyParser() {
    super(new ALCoosaCountyAParser(), new ALCoosaCountyBParser());
  }
}
