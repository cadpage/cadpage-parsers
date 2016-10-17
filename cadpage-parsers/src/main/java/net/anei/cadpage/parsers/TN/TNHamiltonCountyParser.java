package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNHamiltonCountyParser extends GroupBestParser {
  
  public TNHamiltonCountyParser() {
    super(new TNHamiltonCountyAParser(), new TNHamiltonCountyBParser(), new TNHamiltonCountyCParser());
  }
}
