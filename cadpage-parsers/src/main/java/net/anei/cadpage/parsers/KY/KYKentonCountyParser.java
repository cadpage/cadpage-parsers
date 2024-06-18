package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYKentonCountyParser extends GroupBestParser {

  public KYKentonCountyParser() {
    super(new KYKentonCountyAParser(),
          new KYKentonCountyBParser(),
          new KYKentonCountyCParser());
  }
}
