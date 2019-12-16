package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYStatePoliceParser extends GroupBestParser {
  
  public KYStatePoliceParser() {
    super(new KYStatePoliceAParser(), new KYStatePoliceBParser());
  }
}
