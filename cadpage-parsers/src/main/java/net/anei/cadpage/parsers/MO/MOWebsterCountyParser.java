package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOWebsterCountyParser extends GroupBestParser {

  public MOWebsterCountyParser() {
    super(new MOWebsterCountyAParser(), new MOWebsterCountyBParser());
  }
}
