package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALCalhounCountyParser extends GroupBestParser {

  public ALCalhounCountyParser() {
    super(new ALCalhounCountyAParser(),
          new ALCalhounCountyBParser(),
          new ALCalhounCountyDParser());
  }
}
