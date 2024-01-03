package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;

public class NCWayneCountyParser extends GroupBestParser {

  public NCWayneCountyParser() {
    super(new NCWayneCountyAParser(),
          new NCWayneCountyBParser(),
          new NCWayneCountyCParser(),
          new NCWayneCountyDParser(),
          new NCWayneCountyEParser());
  }
}
