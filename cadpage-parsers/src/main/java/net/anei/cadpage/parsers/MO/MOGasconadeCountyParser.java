package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOGasconadeCountyParser extends GroupBestParser {

  public MOGasconadeCountyParser() {
    super(new MOGasconadeCountyAParser(), new MOGasconadeCountyBParser());
  }
}
