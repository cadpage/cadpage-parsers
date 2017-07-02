package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOStFrancoisCountyParser extends GroupBestParser {

  public MOStFrancoisCountyParser() {
    super(new MOStFrancoisCountyAParser(), new MOStFrancoisCountyBParser());
  }
}
