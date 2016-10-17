package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALGenevaCountyParser extends GroupBestParser {
  
  public ALGenevaCountyParser() {
    super(new ALGenevaCountyAParser(), new ALGenevaCountyBParser());
  }
}
