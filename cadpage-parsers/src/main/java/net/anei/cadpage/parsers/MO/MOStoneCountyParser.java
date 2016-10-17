package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOStoneCountyParser extends GroupBestParser {

  public MOStoneCountyParser() {
    super(new MOStoneCountyBParser(), new MOStoneCountyCParser());
  }
}
