package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MILenaweeCountyParser extends GroupBestParser {

  public MILenaweeCountyParser() {
    super(new MILenaweeCountyAParser(),
          new MILenaweeCountyBParser(),
          new MILenaweeCountyCParser(),
          new MILenaweeCountyDParser());
  }
}