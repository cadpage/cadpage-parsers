package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDSomersetCountyParser extends GroupBestParser {

  public MDSomersetCountyParser() {
    super(new MDSomersetCountyAParser(), new MDSomersetCountyBParser());
  }
}
