package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCGeorgetownCountyParser extends GroupBestParser {
  
  public SCGeorgetownCountyParser() {
    super(new SCGeorgetownCountyAParser(), new SCGeorgetownCountyBParser());
  }
}