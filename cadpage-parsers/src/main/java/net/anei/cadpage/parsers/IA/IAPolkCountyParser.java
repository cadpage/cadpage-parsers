package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.GroupBestParser;

public class IAPolkCountyParser extends GroupBestParser {

  public IAPolkCountyParser() {
   super(new IAPolkCountyAParser(),
         new IAPolkCountyBParser());
  }
}
