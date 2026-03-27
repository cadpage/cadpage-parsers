package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYButlerCountyParser extends GroupBestParser {

  public KYButlerCountyParser() {
    super(new KYButlerCountyAParser(), new KYButlerCountyBParser());
  }
}
