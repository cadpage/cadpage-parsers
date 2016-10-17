package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCCharlestonCountyParser extends GroupBestParser {
  
  public SCCharlestonCountyParser() {
    super(new SCCharlestonCountyAParser(), new SCCharlestonCountyBParser());
  }
}
