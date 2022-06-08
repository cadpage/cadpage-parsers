package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNUnionCountyParser extends GroupBestParser {

  public TNUnionCountyParser() {
    super(new TNUnionCountyAParser(), new TNUnionCountyBParser());
  }
}
