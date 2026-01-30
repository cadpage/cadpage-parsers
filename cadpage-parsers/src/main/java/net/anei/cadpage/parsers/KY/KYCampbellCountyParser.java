package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYCampbellCountyParser extends GroupBestParser {

  public KYCampbellCountyParser() {
    super(new KYCampbellCountyAParser(),
          new KYCampbellCountyBParser(),
          new KYCampbellCountyCParser());
  }
}
