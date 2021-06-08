package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INLakeCountyParser extends GroupBestParser {

  public INLakeCountyParser() {
    super(new INLakeCountyAParser(), new INLakeCountyBParser());
  }

}
