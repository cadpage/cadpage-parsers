package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MONewtonCountyParser extends GroupBestParser {

  public MONewtonCountyParser() {
    super(new MONewtonCountyAParser(), new MONewtonCountyBParser());
  }
}
