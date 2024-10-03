package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALMontgomeryCountyParser extends GroupBestParser {

  public ALMontgomeryCountyParser() {
    super(new ALMontgomeryCountyAParser(), new ALMontgomeryCountyBParser());
  }
}
