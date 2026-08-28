package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOWarrenCountyParser extends GroupBestParser {

  public MOWarrenCountyParser() {
    super(new MOWarrenCountyBParser(),
          new MOWarrenCountyCParser());
  }
}
