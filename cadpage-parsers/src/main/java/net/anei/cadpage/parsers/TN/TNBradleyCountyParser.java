package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNBradleyCountyParser extends GroupBestParser {
  
  public TNBradleyCountyParser() {
    super(new TNBradleyCountyAParser(), new TNBradleyCountyBParser());
  }
}
