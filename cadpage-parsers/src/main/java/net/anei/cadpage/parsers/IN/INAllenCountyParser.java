package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INAllenCountyParser extends GroupBestParser {

  public INAllenCountyParser() {
    super(new INAllenCountyAParser(),
          new INAllenCountyBParser());
  }

}
