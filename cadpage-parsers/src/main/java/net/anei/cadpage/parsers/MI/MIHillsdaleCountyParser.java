package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIHillsdaleCountyParser extends GroupBestParser {

  public MIHillsdaleCountyParser() {
    super(new MIHillsdaleCountyAParser(), new MIHillsdaleCountyBParser());
  }
}