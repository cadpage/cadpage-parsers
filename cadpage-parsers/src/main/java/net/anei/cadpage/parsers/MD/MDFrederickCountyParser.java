package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;

public class MDFrederickCountyParser extends GroupBestParser {

  public MDFrederickCountyParser() {
    super(new MDFrederickCountyAParser(),
          new MDFrederickCountyBParser());
  }
}
