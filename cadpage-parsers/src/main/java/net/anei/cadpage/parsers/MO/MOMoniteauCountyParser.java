package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOMoniteauCountyParser extends GroupBestParser {

  public MOMoniteauCountyParser() {
    super(new MOMoniteauCountyAParser(), new MOMoniteauCountyBParser());
  }
}
