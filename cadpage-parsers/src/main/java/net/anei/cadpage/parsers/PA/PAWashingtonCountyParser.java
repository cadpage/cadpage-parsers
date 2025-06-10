package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAWashingtonCountyParser extends GroupBestParser {

  public PAWashingtonCountyParser() {
    super(new PAWashingtonCountyAParser(),
          new PAWashingtonCountyBParser());
  }

}