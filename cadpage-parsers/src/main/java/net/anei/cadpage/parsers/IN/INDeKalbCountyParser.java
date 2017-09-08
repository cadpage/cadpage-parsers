package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INDeKalbCountyParser extends GroupBestParser {
  
  public INDeKalbCountyParser() {
    super(new INDeKalbCountyAParser(), new INDeKalbCountyBParser());
  }

}
