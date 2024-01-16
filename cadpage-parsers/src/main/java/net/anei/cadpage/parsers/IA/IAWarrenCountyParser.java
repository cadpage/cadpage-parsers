package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.GroupBestParser;

public class IAWarrenCountyParser extends GroupBestParser {

  public IAWarrenCountyParser() {
   super(new IAWarrenCountyAParser(),
         new IAWarrenCountyBParser(),
         new IAWarrenCountyDParser());
  }
}
