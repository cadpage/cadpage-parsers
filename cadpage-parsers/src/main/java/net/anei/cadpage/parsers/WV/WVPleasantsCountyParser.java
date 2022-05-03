package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVPleasantsCountyParser extends GroupBestParser {
  
  public WVPleasantsCountyParser() {
    super(new WVPleasantsCountyAParser(), new WVPleasantsCountyBParser());
  }
}
