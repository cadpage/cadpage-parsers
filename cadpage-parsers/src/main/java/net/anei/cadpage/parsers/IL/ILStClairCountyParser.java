package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILStClairCountyParser extends GroupBestParser {
  
  public ILStClairCountyParser() {
    super(new ILStClairCountyAParser(), new ILStClairCountyBParser());

  }
}
