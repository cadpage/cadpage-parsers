package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNBlountCountyParser extends GroupBestParser {
  
  public TNBlountCountyParser() {
    super(new TNBlountCountyAParser(), new TNBlountCountyBParser());
  }
}
