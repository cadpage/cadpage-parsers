package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PATiogaCountyParser extends GroupBestParser {

  public PATiogaCountyParser() {
    super(new PATiogaCountyAParser(),
          new PATiogaCountyBParser());
  }

}