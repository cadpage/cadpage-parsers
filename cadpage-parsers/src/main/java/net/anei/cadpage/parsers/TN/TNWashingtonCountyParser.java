package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNWashingtonCountyParser extends GroupBestParser {
  
  public TNWashingtonCountyParser() {
    super(new TNWashingtonCountyAParser(), new TNWashingtonCountyBParser());
  }
}
