package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDHowardCountyParser extends GroupBestParser {

  public MDHowardCountyParser() {
    super(new MDHowardCountyAParser(),
           new MDHowardCountyBParser());
  }
}
