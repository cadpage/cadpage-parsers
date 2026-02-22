package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYHenryCountyParser extends GroupBestParser {

  public KYHenryCountyParser() {
    super(new KYHenryCountyAParser(),
          new KYHenryCountyBParser(),
          new KYHenryCountyCParser());
  }
}
