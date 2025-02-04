package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PASomersetCountyParser extends GroupBestParser {

  public PASomersetCountyParser() {
    super(new PASomersetCountyBParser(),
          new PASomersetCountyCParser());
  }

}