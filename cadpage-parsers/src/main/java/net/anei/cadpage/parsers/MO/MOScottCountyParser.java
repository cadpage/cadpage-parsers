package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOScottCountyParser extends GroupBestParser {

  public MOScottCountyParser() {
    super(new MOScottCountyAParser(),
          new MOScottCountyBParser(),
          new MOScottCountyCParser());
  }
}
