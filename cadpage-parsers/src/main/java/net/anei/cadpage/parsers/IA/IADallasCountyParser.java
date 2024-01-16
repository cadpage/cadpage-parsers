package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.GroupBestParser;

public class IADallasCountyParser extends GroupBestParser {

  public IADallasCountyParser() {
   super(new IADallasCountyAParser(), new IADallasCountyBParser());
  }
}
