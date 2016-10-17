package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYCarrollCountyParser extends GroupBestParser {
  
  public KYCarrollCountyParser() {
    super(new KYCarrollCountyAParser(), new KYCarrollCountyBParser(),
        new KYCarrollCountyCParser());
  }
}
