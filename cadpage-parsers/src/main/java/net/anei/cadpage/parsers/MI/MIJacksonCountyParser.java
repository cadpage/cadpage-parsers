package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIJacksonCountyParser extends GroupBestParser {

  public MIJacksonCountyParser() {
    super(new MIJacksonCountyAParser(), new MIJacksonCountyBParser());
  }
}