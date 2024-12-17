package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INFloydCountyParser extends GroupBestParser {

  public INFloydCountyParser() {
    super(new INFloydCountyAParser(), new INFloydCountyBParser());
  }

}
