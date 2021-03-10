package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNGraingerCountyParser extends GroupBestParser {

  public TNGraingerCountyParser() {
    super(new TNGraingerCountyAParser(), new TNGraingerCountyBParser());
  }
}
