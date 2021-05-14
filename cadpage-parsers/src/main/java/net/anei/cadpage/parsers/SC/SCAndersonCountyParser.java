package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCAndersonCountyParser extends GroupBestParser {

  public SCAndersonCountyParser() {
    super(new SCAndersonCountyCParser(), new SCAndersonCountyDParser());
  }
}
