package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYMontgomeryCountyParser extends GroupBestParser {

  public NYMontgomeryCountyParser() {
    super(new NYMontgomeryCountyAParser(), new NYMontgomeryCountyBParser());
  }
}
