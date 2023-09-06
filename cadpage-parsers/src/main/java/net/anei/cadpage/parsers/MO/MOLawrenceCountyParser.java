package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOLawrenceCountyParser extends GroupBestParser {

  public MOLawrenceCountyParser() {
    super(new MOLawrenceCountyAParser(), new MOLawrenceCountyBParser());
  }
}
