package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MISaginawCountyParser extends GroupBestParser {

  public MISaginawCountyParser() {
    super(new MISaginawCountyAParser(), new MISaginawCountyBParser(),
          new MISaginawCountyCParser());
  }
}