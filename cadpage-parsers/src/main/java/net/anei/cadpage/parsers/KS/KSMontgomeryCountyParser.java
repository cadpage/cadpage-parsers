package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSMontgomeryCountyParser extends GroupBestParser {

  public KSMontgomeryCountyParser() {
    super(new KSMontgomeryCountyAParser(), new KSMontgomeryCountyBParser());
  }

}
