package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INFountainCountyParser extends GroupBestParser {

  public INFountainCountyParser() {
    super(new INFountainCountyAParser(), new INFountainCountyBParser());
  }

}
