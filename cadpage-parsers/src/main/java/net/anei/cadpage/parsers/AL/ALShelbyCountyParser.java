package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALShelbyCountyParser extends GroupBestParser {

  public ALShelbyCountyParser() {
    super(new ALShelbyCountyAParser(),
          new ALShelbyCountyBParser(),
          new ALShelbyCountyCParser());
  }
}
