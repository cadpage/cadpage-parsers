package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNSevierCountyParser extends GroupBestParser {

  public TNSevierCountyParser() {
    super(new TNSevierCountyAParser(), new TNSevierCountyBParser());
  }
}
