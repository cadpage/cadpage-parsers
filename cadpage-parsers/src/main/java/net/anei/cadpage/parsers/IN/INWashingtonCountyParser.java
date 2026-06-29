package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INWashingtonCountyParser extends GroupBestParser {

  public INWashingtonCountyParser() {
    super(new INWashingtonCountyAParser(), new INWashingtonCountyBParser());
  }

}
