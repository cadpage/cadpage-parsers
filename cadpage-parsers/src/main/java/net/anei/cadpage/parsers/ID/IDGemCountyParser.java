package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDGemCountyParser extends GroupBestParser {

  public IDGemCountyParser() {
   super(new IDGemCountyAParser(), new IDGemCountyBParser());
  }
}
