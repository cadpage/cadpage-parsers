package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INNobleCountyParser extends GroupBestParser {

  public INNobleCountyParser() {
    super(new INNobleCountyAParser(), new INNobleCountyBParser());
  }

}
