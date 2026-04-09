package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYHendersonCountyParser extends GroupBestParser {

  public KYHendersonCountyParser() {
    super(new KYHendersonCountyAParser(), new KYHendersonCountyBParser());
  }
}
