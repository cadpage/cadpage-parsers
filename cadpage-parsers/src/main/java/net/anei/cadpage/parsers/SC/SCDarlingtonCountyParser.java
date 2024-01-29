package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCDarlingtonCountyParser extends GroupBestParser {

  public SCDarlingtonCountyParser() {
    super(new SCDarlingtonCountyAParser(), new SCDarlingtonCountyBParser());
  }
}