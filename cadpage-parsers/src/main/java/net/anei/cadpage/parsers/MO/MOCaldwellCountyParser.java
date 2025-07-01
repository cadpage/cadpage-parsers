package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOCaldwellCountyParser extends GroupBestParser {

  public MOCaldwellCountyParser() {
    super(new MOCaldwellCountyAParser(), new MOCaldwellCountyBParser());
  }
}
