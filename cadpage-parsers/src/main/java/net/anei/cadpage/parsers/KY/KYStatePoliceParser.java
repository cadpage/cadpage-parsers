package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYStatePoliceParser extends GroupBestParser {

  public KYStatePoliceParser() {
    super(new KYStatePoliceBParser(), new KYStatePoliceCParser());
  }

  @Override
  public String getLocName() {
    return "Kentucky State Police";
  }
}
