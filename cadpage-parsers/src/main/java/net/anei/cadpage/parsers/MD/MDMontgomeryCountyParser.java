package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDMontgomeryCountyParser extends GroupBestParser {
  
  public MDMontgomeryCountyParser() {
    super(new MDMontgomeryCountyAParser(),
          new MDMontgomeryCountyBParser(),
          new MDMontgomeryCountyCParser(),
          new MDMontgomeryCountyDParser());
  }
  
}
