package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MILivingstonCountyParser extends GroupBestParser {

  public MILivingstonCountyParser() {
    super(new MILivingstonCountyAParser(), new MILivingstonCountyBParser());
  }
}
