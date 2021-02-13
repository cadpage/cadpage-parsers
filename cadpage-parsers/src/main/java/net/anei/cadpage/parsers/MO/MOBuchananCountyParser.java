package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOBuchananCountyParser extends GroupBestParser {

  public MOBuchananCountyParser() {
    super(new MOBuchananCountyAParser(),
          new MOBuchananCountyBParser(),
          new MOBuchananCountyDParser());
  }
}
