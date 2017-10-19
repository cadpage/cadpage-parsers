package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOPulaskiCountyParser extends GroupBestParser {

  public MOPulaskiCountyParser() {
    super(new MOPulaskiCountyAParser(), new MOPulaskiCountyBParser());
  }
}
