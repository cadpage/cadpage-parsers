package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOPettisCountyParser extends GroupBestParser {

  public MOPettisCountyParser() {
    super(new MOPettisCountyAParser(), new MOPettisCountyBParser());
  }
}
