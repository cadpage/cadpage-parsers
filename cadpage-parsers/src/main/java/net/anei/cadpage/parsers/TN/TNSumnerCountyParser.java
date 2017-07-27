package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNSumnerCountyParser extends GroupBestParser {
  
  public TNSumnerCountyParser() {
    super(new TNSumnerCountyAParser(), new TNSumnerCountyBParser());
  }
}
