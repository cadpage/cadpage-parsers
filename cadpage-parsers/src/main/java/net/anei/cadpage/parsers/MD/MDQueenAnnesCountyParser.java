package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDQueenAnnesCountyParser extends GroupBestParser {

  public MDQueenAnnesCountyParser() {
    super(new MDQueenAnnesCountyBParser(), new MDQueenAnnesCountyCParser());
  }
}
