package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILJacksonCountyParser extends GroupBestParser {

  public ILJacksonCountyParser() {
    super(new ILJacksonCountyAParser(), new ILJacksonCountyBParser());
  }
}
