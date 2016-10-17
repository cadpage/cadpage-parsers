package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INWayneCountyParser extends GroupBestParser {
  
  public INWayneCountyParser() {
    super(new INWayneCountyAParser(), new INWayneCountyBParser());
  }

}
