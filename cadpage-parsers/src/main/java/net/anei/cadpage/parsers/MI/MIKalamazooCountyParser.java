package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;


public class MIKalamazooCountyParser extends GroupBestParser {
  
  public MIKalamazooCountyParser() {
    super(new MIKalamazooCountyAParser(), new MIKalamazooCountyCParser());
  }
  
}
