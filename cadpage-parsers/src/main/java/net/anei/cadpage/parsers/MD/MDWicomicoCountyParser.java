package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDWicomicoCountyParser extends GroupBestParser {
 
  public MDWicomicoCountyParser() {
    super(new MDWicomicoCountyAParser(), new MDWicomicoCountyBParser());
  }
}
