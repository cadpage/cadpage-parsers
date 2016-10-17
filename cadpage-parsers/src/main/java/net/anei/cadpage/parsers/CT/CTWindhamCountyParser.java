package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTWindhamCountyParser extends GroupBestParser {
  
  public CTWindhamCountyParser() {
    super(new CTWindhamCountyAParser(), new CTWindhamCountyBParser(), new CTWindhamCountyCParser());
  }
}
