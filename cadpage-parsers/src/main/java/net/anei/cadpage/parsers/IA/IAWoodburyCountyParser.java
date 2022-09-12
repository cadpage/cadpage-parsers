package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.GroupBestParser;

public class IAWoodburyCountyParser extends GroupBestParser {

  public IAWoodburyCountyParser() {
   super(new IAWoodburyCountyBParser(), new IAWoodburyCountyCParser());
  }
}
