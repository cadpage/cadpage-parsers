package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INAdamsCountyParser extends GroupBestParser {

  public INAdamsCountyParser() {
    super(new INAdamsCountyAParser(),
          new INAdamsCountyBParser());
  }

}
