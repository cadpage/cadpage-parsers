package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.GroupBestParser;

public class SDUnionCountyParser extends GroupBestParser {
  
  public SDUnionCountyParser() {
    super(new SDUnionCountyAParser(), new SDUnionCountyBParser());
  }
}
