package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCAikenCountyParser extends GroupBestParser {

  public SCAikenCountyParser() {
    super(new SCAikenCountyAParser(), new SCAikenCountyBParser());
  }
}
