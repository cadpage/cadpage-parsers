package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIWayneCountyParser extends GroupBestParser {

  public MIWayneCountyParser() {
    super(new MIWayneCountyAParser(),
          new MIWayneCountyBParser(),
          new MIWayneCountyCParser());
  }
}
