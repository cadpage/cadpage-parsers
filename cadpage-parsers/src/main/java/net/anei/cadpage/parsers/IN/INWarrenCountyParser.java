package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INWarrenCountyParser extends GroupBestParser {

  public INWarrenCountyParser() {
    super(new INWarrenCountyAParser(), new INWarrenCountyBParser());
  }

}
